package dev.kraus.ERP.Model.Clientes;


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

@Table(name = "clientes")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 40, nullable = false)
    public String nome;

    @Column(length = 70)
    public String nomeFantasia;

    @Column(length = 240)
    public String razaoSocial;

    @Column(length = 20)
    public String cnpjCpf;

    @Column(length = 18)
    public String telefone;

    @Column(length = 18)
    public String celular;

    @Column(length = 70)
    public String email;

    @Column(length = 9)
    public String cep;

    @Column(length = 70)
    public String rua;

    @Column(length = 70)
    public String bairro;

    @Column(length = 8)
    public String numeroCasa;


    @CreationTimestamp
    public LocalDateTime criadoEm;

    public LocalDateTime editadoEm;

    public LocalDateTime excluidoEm;
}
