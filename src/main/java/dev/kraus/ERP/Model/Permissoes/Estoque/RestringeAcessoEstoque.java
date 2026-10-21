package dev.kraus.ERP.Model.Permissoes.Estoque;

import dev.kraus.ERP.Model.Usuarios.Usuarios;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "permissoes_usuarios_estoque")
public class RestringeAcessoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuarios usuario;

    private boolean podeListarEstoque;
    private boolean podeCadastrarEstoque;
    private boolean podeAtualizarEstoque;
    private boolean podeAtualizarQuantidadeEstoque;
    private boolean podeTransferirEstoque;
}
