package dev.kraus.ERP.Repository.Permissoes;

import dev.kraus.ERP.Model.Permissoes.Clientes.RestringeAcessoClientes;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestringeAcessoRepository
        extends JpaRepository<RestringeAcessoClientes, Long> {



    Optional<RestringeAcessoClientes> findByUsuario(Usuarios usuario);
}