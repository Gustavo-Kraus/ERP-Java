package dev.kraus.ERP.Controller.API.RespostaErros;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> produtoJaCadastrado(
            GlobalException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "erro", ex.getMessage()
                ));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<?> requisicaoInvalida(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("erro", ex.getMessage()));
    }
}
