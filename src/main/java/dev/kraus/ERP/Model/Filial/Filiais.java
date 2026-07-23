package dev.kraus.ERP.Model.Filial;

import dev.kraus.ERP.Model.Endereco;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "filiais")
public class Filiais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String razaoSocial;

    @Column(nullable = false, length = 150)
    private String nomeFantasia;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(length = 20)
    private String inscricaoEstadual;

    @Column(length = 20)
    private String inscricaoMunicipal;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String celular;

    @Column(length = 150)
    private String responsavel;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private Boolean matriz = false;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    @Embedded
    private Endereco endereco;

    private String regimeTributario;

    private String codigoMunicipioIbge;

    private String cnaePrincipal;

    private String suframa;

    private Boolean contribuinteIcms;

    private Boolean simplesNacional;

    public enum RegimeTributario {
        SIMPLES_NACIONAL,
        LUCRO_PRESUMIDO,
        LUCRO_REAL
    }
}