package dev.kraus.ERP.DTO.Produtos;

import dev.kraus.ERP.Model.Enums.Produtos.TipoProduto;
import dev.kraus.ERP.Model.Enums.Produtos.UnidadeMedida;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequest {

    private String codigo;
    private String codigoBarras;
    private String nome;
    private String descricao;

    private UnidadeMedida unidadeMedida;
    private TipoProduto tipoProduto;

}