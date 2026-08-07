package dev.kraus.ERP.DTO.Estoque;

import dev.kraus.ERP.Model.Enums.Produtos.TipoMovimentacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class EstoqueRequestAtt {

    private Long id;
    private BigDecimal quantidade;
    private String metodo;
}
