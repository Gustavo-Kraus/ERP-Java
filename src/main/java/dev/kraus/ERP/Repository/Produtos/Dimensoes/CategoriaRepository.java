package dev.kraus.ERP.Repository.Produtos.Dimensoes;

import dev.kraus.ERP.Model.Produtos.Dimensoes.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
