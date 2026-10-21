package dev.kraus.ERP.Repository.Permissoes;

import dev.kraus.ERP.Model.Permissoes.Produtos.RestringeAcessoProdutos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestringeAcessoProdutosRepository extends JpaRepository<RestringeAcessoProdutos, Long> {

    Optional<RestringeAcessoProdutos> findByUsuario(Usuarios usuario);
}
