package dev.kraus.ERP.Service.Tokens;

import dev.kraus.ERP.Model.Token.ApiToken;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Tokens.ApiTokenRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ApiTokenService {

    private final ApiTokenRepository apiTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public ApiTokenService(ApiTokenRepository apiTokenRepository) {
        this.apiTokenRepository = apiTokenRepository;
    }

    public ApiToken gerarToken(Usuarios usuario) {
        ApiToken apiToken = new ApiToken();
        apiToken.setUsuario(usuario);
        apiToken.setToken(gerarValorToken());
        return apiTokenRepository.save(apiToken);
    }

    public Optional<Usuarios> buscarUsuarioPorToken(String token) {
        return apiTokenRepository.findAtivoByToken(token)
                .map(ApiToken::getUsuario)
                .filter(Usuarios::isEnabled);
    }

    public List<ApiToken> listarTokens(Usuarios usuario) {
        return apiTokenRepository.findByUsuarioOrderByCriadoEmDesc(usuario);
    }

    private String gerarValorToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
