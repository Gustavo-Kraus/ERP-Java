package dev.kraus.ERP.Service.Monitoramento;

import com.sun.management.OperatingSystemMXBean;
import dev.kraus.ERP.Model.Monitoramento.MetricasJVM;
import dev.kraus.ERP.Repository.Monitoramento.MetricasJVMRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class MetricasJVMService {

    private final MetricasJVMRepository metricasJVMRepository;

    public MetricasJVMService(MetricasJVMRepository metricasJVMRepository) {
        this.metricasJVMRepository = metricasJVMRepository;
    }

    @Scheduled(fixedRate = 30_000, initialDelay = 30_000)
    @Transactional
    public void registrarMetricas() {
        var memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage memoriaNaoHeap = memoryBean.getNonHeapMemoryUsage();

        MetricasJVM metricas = new MetricasJVM();
        metricas.setAmostradoEm(LocalDateTime.now());
        metricas.setCpuProcessoPercentual(obterUsoCpuProcesso());
        metricas.setHeapUsadoBytes(heap.getUsed());
        metricas.setHeapComprometidoBytes(heap.getCommitted());
        metricas.setHeapMaximoBytes(heap.getMax() >= 0 ? heap.getMax() : null);
        metricas.setMemoriaNaoHeapUsadaBytes(memoriaNaoHeap.getUsed());
        metricas.setMemoriaNaoHeapComprometidaBytes(memoriaNaoHeap.getCommitted());
        metricas.setMemoriaNaoHeapMaximaBytes(memoriaNaoHeap.getMax() >= 0 ? memoriaNaoHeap.getMax() : null);

        metricasJVMRepository.save(metricas);
    }

    private BigDecimal obterUsoCpuProcesso() {
        java.lang.management.OperatingSystemMXBean operatingSystemBean =
                ManagementFactory.getOperatingSystemMXBean();

        if (!(operatingSystemBean instanceof OperatingSystemMXBean processOperatingSystemBean)) {
            return null;
        }

        double usoCpu = processOperatingSystemBean.getProcessCpuLoad();
        if (usoCpu < 0) {
            return null;
        }

        return BigDecimal.valueOf(usoCpu * 100)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
