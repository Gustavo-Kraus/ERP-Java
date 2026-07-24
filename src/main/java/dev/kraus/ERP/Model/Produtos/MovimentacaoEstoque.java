package dev.kraus.ERP.Model.Produtos;

import dev.kraus.ERP.Model.Enums.Produtos.TipoMovimentacao;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Produtos produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @ManyToOne
    private Almoxarifado origem;

    @ManyToOne
    private Almoxarifado destino;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantidade;

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;

    @ManyToOne
    private Usuarios usuario;

    @Column(length = 100)
    private String documento;

    @Column(length = 500)
    private String observacao;

}

