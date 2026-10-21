package dev.kraus.ERP.Controller.Tokens;

import dev.kraus.ERP.Model.Token.ApiToken;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Service.Tokens.ApiTokenService;
import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/tokens")
public class ApiTokenIssuanceController {

    private final ApiTokenService apiTokenService;
    private final UsuariosService usuariosService;

    public ApiTokenIssuanceController(ApiTokenService apiTokenService, UsuariosService usuariosService) {
        this.apiTokenService = apiTokenService;
        this.usuariosService = usuariosService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarTokenRequest request) {
        if (request.email() == null || request.email().isBlank()
                || request.senha() == null || request.duracao() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Informe email, senha e duração do token."));
        }

        return usuariosService.autenticarCredenciais(request.email().trim(), request.senha())
                .<ResponseEntity<?>>map(usuario -> criarToken(usuario, request.duracao()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("erro", "Credenciais inválidas.")));
    }

    private ResponseEntity<TokenResponse> criarToken(Usuarios usuario, DuracaoToken duracao) {
        LocalDateTime expiraEm = duracao.dias == null
                ? null
                : LocalDateTime.now().plusDays(duracao.dias);
        ApiToken token = apiTokenService.gerarToken(usuario, expiraEm);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TokenResponse(token.getToken(), token.getExpiraEm()));
    }

    public record CriarTokenRequest(
            @JsonAlias({"usuario", "username"}) String email,
            @JsonAlias("password") String senha,
            DuracaoToken duracao
    ) {
    }

    public record TokenResponse(String token, LocalDateTime expiraEm) {
    }

    public enum DuracaoToken {
        DIAS_30(30),
        DIAS_60(60),
        DIAS_120(120),
        PERMANENTE(null);

        private final Integer dias;

        DuracaoToken(Integer dias) {
            this.dias = dias;
        }
    }
}
