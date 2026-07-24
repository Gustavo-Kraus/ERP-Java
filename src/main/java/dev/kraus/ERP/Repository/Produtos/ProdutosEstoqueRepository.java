package dev.kraus.ERP.Repository.Produtos;

import dev.kraus.ERP.Model.Produtos.ProdutoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosEstoqueRepository extends JpaRepository<ProdutoEstoque, Long> {
}
