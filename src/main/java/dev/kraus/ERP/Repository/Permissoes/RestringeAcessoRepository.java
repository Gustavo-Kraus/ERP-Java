package dev.kraus.ERP.Repository.Permissoes;

import dev.kraus.ERP.Model.Permissoes.RestringeAcesso;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestringeAcessoRepository
        extends JpaRepository<RestringeAcesso, Long> {



    Optional<RestringeAcesso> findByUsuario(Usuarios usuario);
}