package dev.kraus.ERP.Service.Logs;

public record DadosAcessoSite(
        String ip,
        String origemIp,
        String xForwardedFor,
        String metodo,
        String caminho,
        String queryString,
        Integer statusHttp,
        Long duracaoMs,
        String userAgent,
        String referer,
        String origin,
        String idiomaAceito,
        String sessionId,
        String requestId,
        String usuarioEmail,
        Long usuarioId
) {
}
