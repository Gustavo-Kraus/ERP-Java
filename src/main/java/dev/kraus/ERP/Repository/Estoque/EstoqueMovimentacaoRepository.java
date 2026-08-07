package dev.kraus.ERP.Repository.Estoque;

import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Estoque.EstoqueMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueMovimentacaoRepository extends JpaRepository <EstoqueMovimentacao, Long> {
}
