package dev.kraus.ERP.Repository.Permissoes;

import dev.kraus.ERP.Model.Permissoes.Pedidos.RestringeAcessoPedidos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestringeAcessoPedidosRepository extends JpaRepository<RestringeAcessoPedidos, Long> {

    Optional<RestringeAcessoPedidos> findByUsuario(Usuarios usuario);
}
