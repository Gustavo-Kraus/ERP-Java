package dev.kraus.ERP.Repository.Usuarios;


import dev.kraus.ERP.Model.Usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository  extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByEmail(String email);
}
