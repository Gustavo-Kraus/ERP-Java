package dev.kraus.ERP.DTO.Estoque;

import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Model.Produtos.Produtos;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EstoqueRequest {

    private Long estoque;
    private Long produto;
    private Long almoxarifado;
    private BigDecimal quantidadeAtual;
    private BigDecimal quantidadeReservada;
    private BigDecimal estoqueMinimo;
    private BigDecimal estoqueMaximo;
    private BigDecimal pontoReposicao;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private String atualizar;



}
