package dev.kraus.ERP.Repository.Permissoes;

import dev.kraus.ERP.Model.Permissoes.Estoque.RestringeAcessoEstoque;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestringeAcessoEstoqueRepository extends JpaRepository<RestringeAcessoEstoque, Long> {

    Optional<RestringeAcessoEstoque> findByUsuario(Usuarios usuario);
}
