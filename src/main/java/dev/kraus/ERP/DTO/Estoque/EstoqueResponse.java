package dev.kraus.ERP.DTO.Estoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EstoqueResponse(
        Long id,
        BigDecimal quantidadeAtual,
        BigDecimal quantidadeReservada,
        BigDecimal estoqueMinimo,
        BigDecimal estoqueMaximo,
        BigDecimal pontoReposicao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
