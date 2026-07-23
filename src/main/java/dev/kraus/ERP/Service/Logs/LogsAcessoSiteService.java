package dev.kraus.ERP.Service.Logs;


import dev.kraus.ERP.Model.Usuarios.Usuarios;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LogsAcessoSiteService {

    private final LogsAcessoSiteAsyncService logsAcessoSiteAsyncService;

    public LogsAcessoSiteService(LogsAcessoSiteAsyncService logsAcessoSiteAsyncService) {
        this.logsAcessoSiteAsyncService = logsAcessoSiteAsyncService;
    }

    public void salvarAcesso(
            HttpServletRequest request,
            HttpServletResponse response,
            long duracaoMs,
            Authentication authentication
    ) {

        String ip = extrairIp(request);
        DadosUsuario dadosUsuario = extrairUsuario(authentication);

        DadosAcessoSite dados = new DadosAcessoSite(
                ip,
                extrairOrigemIp(request),
                request.getHeader("X-Forwarded-For"),
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                response.getStatus(),
                duracaoMs,
                request.getHeader("User-Agent"),
                request.getHeader("Referer"),
                request.getHeader("Origin"),
                request.getHeader("Accept-Language"),
                extrairSessionId(request),
                extrairRequestId(request),
                dadosUsuario.email(),
                dadosUsuario.id()
        );

        logsAcessoSiteAsyncService.salvar(dados);
    }

    private String extrairIp(HttpServletRequest request) {

        String cfConnectingIp = primeiroValorValido(request.getHeader("CF-Connecting-IP"));
        if (cfConnectingIp != null) {
            return cfConnectingIp;
        }

        String xForwardedFor = primeiroValorValido(request.getHeader("X-Forwarded-For"));
        if (xForwardedFor != null) {
            return xForwardedFor;
        }

        String xRealIp = primeiroValorValido(request.getHeader("X-Real-IP"));
        if (xRealIp != null) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private String extrairOrigemIp(HttpServletRequest request) {
        if (primeiroValorValido(request.getHeader("CF-Connecting-IP")) != null) {
            return "CF-Connecting-IP";
        }

        if (primeiroValorValido(request.getHeader("X-Forwarded-For")) != null) {
            return "X-Forwarded-For";
        }

        if (primeiroValorValido(request.getHeader("X-Real-IP")) != null) {
            return "X-Real-IP";
        }

        return "remoteAddr";
    }

    private String primeiroValorValido(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String primeiroValor = valor.split(",")[0].trim();

        if (primeiroValor.isBlank() || "unknown".equalsIgnoreCase(primeiroValor)) {
            return null;
        }

        return primeiroValor;
    }

    private String extrairSessionId(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return null;
        }

        return request.getSession(false).getId();
    }

    private String extrairRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");

        if (requestId == null || requestId.isBlank()) {
            requestId = request.getHeader("X-Correlation-ID");
        }

        return requestId;
    }

    private DadosUsuario extrairUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new DadosUsuario(null, null);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Usuarios usuario) {
            return new DadosUsuario(usuario.getId(), usuario.getEmail());
        }

        return new DadosUsuario(null, authentication.getName());
    }

    private record DadosUsuario(Long id, String email) {
    }
}
