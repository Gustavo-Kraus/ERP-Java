package dev.kraus.ERP.Model.Logs.LogsAcessoSite;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "logs_acesso_site")
public class LogsAcessoSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 45)
    public String ip;

    @Column(length = 50)
    public String origemIp;

    @Column(columnDefinition = "TEXT")
    public String xForwardedFor;

    @Column(length = 10)
    public String metodo;

    @Column(columnDefinition = "TEXT")
    public String caminho;

    @Column(columnDefinition = "TEXT")
    public String queryString;

    public Integer statusHttp;

    public Long duracaoMs;

    @Column(columnDefinition = "TEXT")
    public String userAgent;

    @Column(columnDefinition = "TEXT")
    public String referer;

    @Column(columnDefinition = "TEXT")
    public String origin;

    @Column(length = 100)
    public String idiomaAceito;

    @Column(length = 100)
    public String navegador;

    @Column(length = 100)
    public String sistemaOperacional;

    @Column(length = 50)
    public String tipoDispositivo;

    public Boolean bot;

    @Column(length = 120)
    public String usuarioEmail;

    public Long usuarioId;

    @Column(length = 120)
    public String sessionId;

    @Column(length = 100)
    public String requestId;

    @Column(length = 100)
    public String pais;

    @Column(length = 10)
    public String codigoPais;

    @Column(length = 100)
    public String regiao;

    @Column(length = 100)
    public String cidade;

    @Column(length = 30)
    public String cep;

    public Double latitude;

    public Double longitude;

    @Column(length = 100)
    public String timezone;

    @Column(length = 200)
    public String provedorInternet;

    @Column(length = 200)
    public String organizacao;

    @Column(length = 200)
    public String asn;

    public Boolean proxy;

    public Boolean hosting;

    public Boolean mobile;

    @Column(length = 100)
    public String fonteGeo;

    @Column(columnDefinition = "TEXT")
    public String erroGeo;

    @CreationTimestamp
    public LocalDateTime acessadoEm;

}
