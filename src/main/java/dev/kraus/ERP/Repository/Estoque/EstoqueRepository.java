package dev.kraus.ERP.Repository.Estoque;

import dev.kraus.ERP.Model.Estoque.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoId(Long produtoId);


    Optional<Estoque> findByProdutoIdAndAlmoxarifadoId(
            Long produtoId,
            Long almoxarifadoId);
}
