package dev.kraus.ERP.Model.Financeiro;

import dev.kraus.ERP.Model.Enums.Financeiro.ClasseContaContabil;
import dev.kraus.ERP.Model.Enums.Financeiro.GrupoDre;
import dev.kraus.ERP.Model.Enums.Financeiro.NaturezaContaContabil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plano_contas")
@Getter
@Setter
public class PlanoContas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ClasseContaContabil classe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NaturezaContaContabil natureza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private GrupoDre grupoDre = GrupoDre.NAO_APLICAVEL;

    @Column(nullable = false)
    private Boolean analitica = true;

    @Column(nullable = false)
    private Boolean ativa = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_pai_id")
    private PlanoContas contaPai;

    @OneToMany(mappedBy = "contaPai")
    private List<PlanoContas> contasFilhas = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime atualizadoEm;
}
