package dev.kraus.ERP.Config.DadosInicias.Estoque;


import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Repository.Estoque.EstoqueRepository;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.AlmoxarifadoRepository;
import dev.kraus.ERP.Repository.Produtos.ProdutosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EstoqueDadosInicial {

    private final EstoqueRepository estoqueRepository;
    private final ProdutosRepository produtosRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;

    public EstoqueDadosInicial(EstoqueRepository estoqueRepository, ProdutosRepository produtosRepository, AlmoxarifadoRepository almoxarifadoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtosRepository = produtosRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
    }

    //aqui eu puxo se está como modo desenvoledor ativo, se tiver o if vai impedir de gerar novas linhas
    @Value("${developer-mode}")
    private boolean modoDesenvolvedor;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {
        if (!modoDesenvolvedor) {
            System.out.println("Modo produção detectado. Não será criado usuário padrão.");
            return;
        }
        if (estoqueRepository.count() > 0) {
            System.out.println("Estoque já existe. Inicialização finalizada.");
            return;
        }
        Produtos produto = produtosRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Nenhum produto encontrado. Não foi possível criar o estoque padrão."
                        )
                );
        Almoxarifado almoxarifado = almoxarifadoRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Nenhum almoxarifado encontrado. Não foi possível criar o estoque padrão."
                        )
                );

        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setAlmoxarifado(almoxarifado);
        estoque.setEstoqueMinimo(new BigDecimal("50.000"));
        estoque.setEstoqueMaximo(new BigDecimal("100.000"));
        estoque.setPontoReposicao(new BigDecimal("100.000"));
        estoque.setQuantidadeAtual(new BigDecimal("100.000"));
        estoque.setQuantidadeReservada(new BigDecimal("00.000"));
        estoqueRepository.save(estoque);

    }
}
