package dev.kraus.ERP.Config.DadosInicias.Almoxarifado;

import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Repository.Filiais.FiliaisRepository;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.AlmoxarifadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@Order(2)
public class AlmoxarifadoDadosInicial {

    private final AlmoxarifadoRepository almoxarifadoRepository;
    private final FiliaisRepository filiaisRepository;

    public AlmoxarifadoDadosInicial(AlmoxarifadoRepository almoxarifadoRepository, FiliaisRepository filiaisRepository) {
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.filiaisRepository = filiaisRepository;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {

        if (!almoxarifadoRepository.existsById(1L)) {

            Filiais filial = filiaisRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Filial padrão não encontrada"));

            Almoxarifado almoxarifado = new Almoxarifado();
            almoxarifado.setAlmoxarifado("Padrão");
            almoxarifado.setFiliais(filial);

            almoxarifadoRepository.save(almoxarifado);
        }
    }
}
