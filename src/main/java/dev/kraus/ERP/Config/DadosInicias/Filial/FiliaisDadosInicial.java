package dev.kraus.ERP.Config.DadosInicias.Filial;

import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Repository.Filiais.FiliaisRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Order(1)
@Component
public class FiliaisDadosInicial implements ApplicationRunner {

    private final FiliaisRepository filiaisRepository;

    public FiliaisDadosInicial(FiliaisRepository filiaisRepository) {
        this.filiaisRepository = filiaisRepository;
    }

    //aqui eu puxo se está como modo desenvoledor ativo, se tiver o if vai impedir de gerar novas linhas
    @Value("${developer-mode}")
    private boolean modoDesenvolvedor;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!modoDesenvolvedor) {
            System.out.println("Modo produção detectado. Não será criado usuário padrão.");
            return;
        }
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
    }
}