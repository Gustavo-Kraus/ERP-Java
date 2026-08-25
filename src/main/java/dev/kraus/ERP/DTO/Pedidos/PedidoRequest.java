package dev.kraus.ERP.DTO.Pedidos;

import java.math.BigDecimal;
import java.util.List;

public record PedidoRequest(

        Long clienteId,

        List<PedidoItemRequest> itens,

        BigDecimal desconto,

        BigDecimal acrescimo,

        String observacao

) {
}