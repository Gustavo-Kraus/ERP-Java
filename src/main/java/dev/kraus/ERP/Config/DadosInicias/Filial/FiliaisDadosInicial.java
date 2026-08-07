package dev.kraus.ERP.Config.DadosInicias.Filial;

import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Repository.Filiais.FiliaisRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
public class FiliaisDadosInicial {

    private final FiliaisRepository filiaisRepository;

    public FiliaisDadosInicial(FiliaisRepository filiaisRepository) {
        this.filiaisRepository = filiaisRepository;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {


        if (!filiaisRepository.existsById(1L)) {

            Filiais filial = new Filiais();

            filial.setAtivo(true);
            filial.setCriadoEm(LocalDateTime.now());
            filial.setCnpj("00000000000000");
            filial.setCodigo("1");
            filial.setMatriz(true);
            filial.setNomeFantasia("Padrão");
            filial.setRazaoSocial("Padrão");

            filiaisRepository.save(filial);
        }
    }
}