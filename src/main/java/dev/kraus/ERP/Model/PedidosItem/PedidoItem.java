package dev.kraus.ERP.Model.PedidosItem;

import dev.kraus.ERP.Model.Pedidos.Pedido;
import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Produtos.Produtos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "pedidos_itens")
@Getter
@Setter
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id")
    private Estoque estoque;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantidade;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal acrescimo = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal total;

    @Column(nullable = true)
    private String observacao;
}