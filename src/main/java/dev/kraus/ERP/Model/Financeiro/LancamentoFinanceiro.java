package dev.kraus.ERP.Model.Financeiro;

import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Enums.Financeiro.TipoContaFinanceira;
import dev.kraus.ERP.Model.Enums.Financeiro.TipoContaFinanceiraConverter;
import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Model.Pedidos.Pedido;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lancamentos_financeiros")
@Getter
@Setter
public class LancamentoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TipoContaFinanceiraConverter.class)
    @Column(name = "tipo_conta", nullable = false, length = 1)
    private TipoContaFinanceira tipoConta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filial_id", nullable = false)
    private Filiais filial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Clientes cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plano_contas_id", nullable = false)
    private PlanoContas planoContas;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(length = 100)
    private String numeroDocumento;

    @Column(nullable = false)
    private LocalDate dataEmissao;

    @Column(nullable = false)
    private LocalDate dataCompetencia;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal juros = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal multa = BigDecimal.ZERO;

    @Column
    private Integer numeroParcela;

    @Column
    private Integer totalParcelas;

    @Column(length = 1000)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por_id")
    private Usuarios criadoPor;

    @OneToMany(
            mappedBy = "lancamentoFinanceiro",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @OrderBy("dataPagamento ASC, id ASC")
    private List<BaixaFinanceira> baixas = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    @Column
    private LocalDateTime canceladoEm;

    @Column(length = 500)
    private String motivoCancelamento;

    public void adicionarBaixa(BaixaFinanceira baixa) {
        baixa.setLancamentoFinanceiro(this);
        baixas.add(baixa);
    }

    @Transient
    public BigDecimal getValorTotal() {
        return valor.subtract(desconto).add(juros).add(multa);
    }

    @Transient
    public BigDecimal getValorLiquidado() {
        return baixas.stream()
                .map(BaixaFinanceira::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public BigDecimal getValorEmAberto() {
        return getValorTotal().subtract(getValorLiquidado());
    }

    @Transient
    public boolean isQuitado() {
        return getValorEmAberto().signum() <= 0;
    }
}
