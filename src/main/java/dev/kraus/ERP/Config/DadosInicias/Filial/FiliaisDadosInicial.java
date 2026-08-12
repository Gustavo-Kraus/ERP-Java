package dev.kraus.ERP.Config.DadosInicias.Filial;

import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Repository.Filiais.FiliaisRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FiliaisDadosInicial implements ApplicationRunner {

    private final FiliaisRepository filiaisRepository;

    public FiliaisDadosInicial(FiliaisRepository filiaisRepository) {
        this.filiaisRepository = filiaisRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (filiaisRepository.count() > 0) {
            System.out.println("Filial padrão já existe.");
            return;
        }

        Filiais filial = new Filiais();

        filial.setAtivo(true);
        filial.setCriadoEm(LocalDateTime.now());
        filial.setCnpj("00000000000000");
        filial.setCodigo("1");
        filial.setMatriz(true);
        filial.setNomeFantasia("Padrão");
        filial.setRazaoSocial("Padrão");

        Filiais salva = filiaisRepository.save(filial);

        System.out.println("Filial criada. ID: " + salva.getId());
    }
}