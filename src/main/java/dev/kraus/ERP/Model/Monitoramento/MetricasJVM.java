package dev.kraus.ERP.Model.Monitoramento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metricas_jvm")
@Getter
@Setter
public class MetricasJVM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime amostradoEm;

    @Column(precision = 5, scale = 2)
    private BigDecimal cpuProcessoPercentual;

    @Column(nullable = false)
    private Long heapUsadoBytes;

    @Column(nullable = false)
    private Long heapComprometidoBytes;

    private Long heapMaximoBytes;

    @Column(nullable = false)
    private Long memoriaNaoHeapUsadaBytes;

    @Column(nullable = false)
    private Long memoriaNaoHeapComprometidaBytes;

    private Long memoriaNaoHeapMaximaBytes;
}
