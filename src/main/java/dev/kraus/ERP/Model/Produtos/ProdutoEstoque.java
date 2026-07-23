package dev.kraus.ERP.Model.Produtos;

import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "produtoEstoque")
public class ProdutoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Produtos produto;

    @ManyToOne(optional = false)
    private Almoxarifado almoxarifado;

    @Column(precision = 15, scale = 3, nullable = false)
    private BigDecimal quantidadeAtual;

    @Column(precision = 15, scale = 3)
    private BigDecimal quantidadeReservada;

    @Column(precision = 15, scale = 3)
    private BigDecimal estoqueMinimo;

    @Column(precision = 15, scale = 3)
    private BigDecimal estoqueMaximo;

    @Column(precision = 15, scale = 3)
    private BigDecimal pontoReposicao;
}