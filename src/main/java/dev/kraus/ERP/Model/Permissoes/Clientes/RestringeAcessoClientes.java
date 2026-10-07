package dev.kraus.ERP.Model.Permissoes.Clientes;


import dev.kraus.ERP.Model.Usuarios.Usuarios;
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
@Table(name= "permissoes_usuarios_clientes")
public class RestringeAcessoClientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuarios usuario;

    private boolean podeCadastrarCliente;

    private boolean podeExcluirCliente;

    private boolean podeListarCliente;

    private boolean podeEditarCliente;













}
