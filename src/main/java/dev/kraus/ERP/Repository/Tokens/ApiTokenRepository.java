package dev.kraus.ERP.Repository.Tokens;

import dev.kraus.ERP.Model.Token.ApiToken;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {

    @Query("""
            select t from ApiToken t
            join fetch t.usuario
            where t.token = :token
              and t.revogadoEm is null
            """)
    Optional<ApiToken> findAtivoByToken(@Param("token") String token);

    List<ApiToken> findByUsuarioOrderByCriadoEmDesc(Usuarios usuario);
}
