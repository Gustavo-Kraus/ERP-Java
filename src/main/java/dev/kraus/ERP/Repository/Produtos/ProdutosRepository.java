package dev.kraus.ERP.Repository.Produtos;

import dev.kraus.ERP.Model.Produtos.Produtos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    @Query("""
        select p from Produtos p
        where p.desativadoEm is null
        order by p.criadoEm desc nulls last
    """)
    List<Produtos> findByDesativadoEmIsNullOrderByCriadoEmDesc(Pageable pageable);

    @Query("""
        select p from Produtos p
        where p.desativadoEm is null
          and (
            lower(coalesce(p.nome, '')) like lower(concat('%', :busca, '%'))
          )
        order by p.criadoEm desc nulls last
    """)
    List<Produtos> buscarAtivos(@Param("busca") String busca, Pageable pageable);

    boolean existsByCodigoOrCodigoBarras(String codigo, String codigoBarras);
}
