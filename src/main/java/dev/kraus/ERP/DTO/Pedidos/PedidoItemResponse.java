package dev.kraus.ERP.DTO.Pedidos;

import java.math.BigDecimal;

public record PedidoItemResponse(
        Long estoqueId,
        Long produtoId,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal desconto,
        BigDecimal total
) {
}
