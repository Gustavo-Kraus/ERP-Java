package dev.kraus.ERP.Model.Enums.Financeiro;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoContaFinanceiraConverter implements AttributeConverter<TipoContaFinanceira, String> {

    @Override
    public String convertToDatabaseColumn(TipoContaFinanceira tipoContaFinanceira) {
        if (tipoContaFinanceira == null) {
            return null;
        }

        return switch (tipoContaFinanceira) {
            case PAGAR -> "P";
            case RECEBER -> "R";
        };
    }

    @Override
    public TipoContaFinanceira convertToEntityAttribute(String tipoConta) {
        if (tipoConta == null) {
            return null;
        }

        return switch (tipoConta) {
            case "P" -> TipoContaFinanceira.PAGAR;
            case "R" -> TipoContaFinanceira.RECEBER;
            default -> throw new IllegalArgumentException("Tipo de conta financeira inválido: " + tipoConta);
        };
    }
}
