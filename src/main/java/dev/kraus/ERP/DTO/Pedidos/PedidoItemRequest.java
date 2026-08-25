package dev.kraus.ERP.DTO.Pedidos;

import java.math.BigDecimal;

public record PedidoItemRequest(

        Long estoqueId,

        BigDecimal quantidade,

        BigDecimal desconto,

        BigDecimal precoVenda

) {
}
