package dev.kraus.ERP.Controller.API.RespostaErros;

public class GlobalException extends RuntimeException {

    public GlobalException(String mensagem) {
        super(mensagem);
    }
}
