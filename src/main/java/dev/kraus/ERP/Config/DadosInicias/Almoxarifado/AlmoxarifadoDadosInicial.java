package dev.kraus.ERP.Config.DadosInicias.Almoxarifado;

import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Repository.Filiais.FiliaisRepository;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.AlmoxarifadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AlmoxarifadoDadosInicial {

    private final AlmoxarifadoRepository almoxarifadoRepository;
    private final FiliaisRepository filiaisRepository;

    public AlmoxarifadoDadosInicial(
            AlmoxarifadoRepository almoxarifadoRepository,
            FiliaisRepository filiaisRepository
    ) {
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.filiaisRepository = filiaisRepository;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {

        if (almoxarifadoRepository.count() > 0) {
            System.out.println("Almoxarifado já existe. Inicialização finalizada.");
            return;
        }

        Filiais filial = filiaisRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Nenhuma filial encontrada. Não foi possível criar o almoxarifado padrão."
                        )
                );

        Almoxarifado almoxarifado = new Almoxarifado();

        almoxarifado.setAlmoxarifado("Padrão");
        almoxarifado.setFiliais(filial);

        Almoxarifado almoxarifadoSalvo =
                almoxarifadoRepository.save(almoxarifado);

        System.out.println(
                "Almoxarifado padrão criado com sucesso. ID: "
                        + almoxarifadoSalvo.getId()
        );
    }
}
