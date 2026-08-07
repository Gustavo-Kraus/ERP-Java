package dev.kraus.ERP.DTO.Estoque;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class EstoqueRequestMEA {


    private Long idEstoqueOrigem;
    private Long idEstoqueDestino;
    private BigDecimal quantidade;
}
