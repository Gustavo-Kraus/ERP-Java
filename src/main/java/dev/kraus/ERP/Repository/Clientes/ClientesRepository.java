package dev.kraus.ERP.Repository.Clientes;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.kraus.ERP.Model.Clientes.Clientes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ClientesRepository extends JpaRepository<Clientes, Long> {

    List<Clientes> findByExcluidoEmIsNullOrderByCriadoEmDesc(Pageable pageable);

    @Query("""
        select c from Clientes c
        where c.excluidoEm is null
          and (
            lower(coalesce(c.nome, '')) like lower(concat('%', :busca, '%'))
            or lower(coalesce(c.email, '')) like lower(concat('%', :busca, '%'))
            or lower(coalesce(c.cnpjCpf, '')) like lower(concat('%', :busca, '%'))
            or lower(coalesce(c.nomeFantasia, '')) like lower(concat('%', :busca, '%'))
            or lower(coalesce(c.razaoSocial, '')) like lower(concat('%', :busca, '%'))
          )
        order by c.criadoEm desc
    """)
    List<Clientes> buscarAtivos(@Param("busca") String busca, Pageable pageable);

    List<Clientes> findByExcluidoEmIsNullAndId(Long id);

    List<Clientes> findByIdAndExcluidoEmIsNull(Long id);

    boolean existsByIdAndExcluidoEmIsNull(Long id);
}
