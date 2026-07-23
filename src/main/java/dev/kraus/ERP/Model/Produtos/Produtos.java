package dev.kraus.ERP.Model.Produtos;

import dev.kraus.ERP.Model.Enums.Produtos.TipoProduto;
import dev.kraus.ERP.Model.Enums.Produtos.UnidadeMedida;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Categoria;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Marca;
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

@Table(name = "produtos")
public class Produtos {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 50)
        private String codigo;

        @Column(length = 50)
        private String codigoBarras;

        @Column(nullable = false, length = 150)
        private String nome;

        @Column(length = 500)
        private String descricao;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private UnidadeMedida unidadeMedida;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TipoProduto tipoProduto;

        @ManyToOne
        private Categoria categoria;

        @ManyToOne
        private Marca marca;

        private String modelo;

        private String fabricante;

        private String referenciaFabricante;

        private String ncm;

        private String cest;

        private String origemMercadoria;

        private String cfopVenda;

        private String cfopCompra;

        private String cstIcms;

        private String csosn;

        private String cstPis;

        private String cstCofins;

        private String cstIpi;

        @Column(precision = 5, scale = 2)
        private BigDecimal aliquotaIcms;

        @Column(precision = 5, scale = 2)
        private BigDecimal aliquotaPis;

        @Column(precision = 5, scale = 2)
        private BigDecimal aliquotaCofins;

        @Column(precision = 5, scale = 2)
        private BigDecimal aliquotaIpi;

        private Boolean ativo = true;

        private Boolean controlaEstoque = true;

        private Boolean aceitaQuantidadeFracionada = false;

        private Boolean permiteEstoqueNegativo = false;

        private Boolean exigeLote = false;

        private Boolean exigeNumeroSerie = false;

        private LocalDateTime criadoEm;

        private LocalDateTime atualizadoEm;

        private LocalDateTime desativoEm;

        @Column(precision = 10, scale = 3)
        private BigDecimal pesoLiquido;

        @Column(precision = 10, scale = 3)
        private BigDecimal pesoBruto;

        @Column(precision = 10, scale = 2)
        private BigDecimal altura;

        @Column(precision = 10, scale = 2)
        private BigDecimal largura;

        @Column(precision = 10, scale = 2)
        private BigDecimal comprimento;

        @Column(precision = 10, scale = 3)
        private BigDecimal volume;
    }
