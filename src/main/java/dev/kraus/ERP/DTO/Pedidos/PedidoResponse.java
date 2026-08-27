package dev.kraus.ERP.DTO.Pedidos;

import dev.kraus.ERP.Model.Pedidos.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long numero,
        Long clienteId,
        StatusPedido status,
        BigDecimal subtotal,
        BigDecimal desconto,
        BigDecimal acrescimo,
        BigDecimal total,
        String observacao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        List<PedidoItemResponse> itens
) {
}
