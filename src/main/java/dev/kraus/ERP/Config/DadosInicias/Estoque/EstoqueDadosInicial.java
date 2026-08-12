package dev.kraus.ERP.Config.DadosInicias.Estoque;


import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Repository.Estoque.EstoqueRepository;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.AlmoxarifadoRepository;
import dev.kraus.ERP.Repository.Produtos.ProdutosRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {
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
        System.out.println("Estoque padrão criado com sucesso. ID: " + estoque.getId());

    }
}
