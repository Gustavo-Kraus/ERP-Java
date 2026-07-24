package dev.kraus.ERP.Mapper.Produtos;

import dev.kraus.ERP.DTO.Produtos.ProdutoRequest;
import dev.kraus.ERP.Model.Produtos.Produtos;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produtos toEntity(ProdutoRequest request){

        Produtos produto = new Produtos();

        produto.setCodigo(request.getCodigo());
        produto.setCodigoBarras(request.getCodigoBarras());
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());

        produto.setUnidadeMedida(request.getUnidadeMedida());
        produto.setTipoProduto(request.getTipoProduto());


        return produto;
    }
}