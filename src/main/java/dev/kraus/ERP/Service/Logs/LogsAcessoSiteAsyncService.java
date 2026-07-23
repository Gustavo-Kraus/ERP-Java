package dev.kraus.ERP.Service.Logs;




import dev.kraus.ERP.Model.Logs.LogsAcessoSite.LogsAcessoSite;
import dev.kraus.ERP.Repository.Logs.LogsAcessoSiteRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class LogsAcessoSiteAsyncService {

    private final LogsAcessoSiteRepository logsAcessoSiteRepository;
    private final GeoIpService geoIpService;

    public LogsAcessoSiteAsyncService(
            LogsAcessoSiteRepository logsAcessoSiteRepository,
            GeoIpService geoIpService
    ) {
        this.logsAcessoSiteRepository = logsAcessoSiteRepository;
        this.geoIpService = geoIpService;
    }

    @Async
    @Transactional
    public void salvar(DadosAcessoSite dados) {

        LogsAcessoSite log = new LogsAcessoSite();

        log.setIp(dados.ip());
        log.setOrigemIp(dados.origemIp());
        log.setXForwardedFor(dados.xForwardedFor());
        log.setMetodo(dados.metodo());
        log.setCaminho(dados.caminho());
        log.setQueryString(dados.queryString());
        log.setStatusHttp(dados.statusHttp());
        log.setDuracaoMs(dados.duracaoMs());
        log.setUserAgent(dados.userAgent());
        log.setReferer(dados.referer());
        log.setOrigin(dados.origin());
        log.setIdiomaAceito(dados.idiomaAceito());
        log.setSessionId(dados.sessionId());
        log.setRequestId(dados.requestId());
        log.setUsuarioEmail(dados.usuarioEmail());
        log.setUsuarioId(dados.usuarioId());
        log.setNavegador(extrairNavegador(dados.userAgent()));
        log.setSistemaOperacional(extrairSistemaOperacional(dados.userAgent()));
        log.setTipoDispositivo(extrairTipoDispositivo(dados.userAgent()));
        log.setBot(isBot(dados.userAgent()));

        preencherGeolocalizacao(log, dados.ip());

        logsAcessoSiteRepository.save(log);
    }

    private void preencherGeolocalizacao(LogsAcessoSite log, String ip) {
        try {
            GeoIpResponse geoIp = geoIpService.buscar(ip);

            if (geoIp == null) {
                return;
            }

            log.setFonteGeo("ip-api.com");

            if (!"success".equalsIgnoreCase(geoIp.status())) {
                log.setErroGeo(geoIp.message());
                return;
            }

            log.setPais(geoIp.country());
            log.setCodigoPais(geoIp.countryCode());
            log.setRegiao(geoIp.regionName());
            log.setCidade(geoIp.city());
            log.setCep(geoIp.zip());
            log.setLatitude(geoIp.lat());
            log.setLongitude(geoIp.lon());
            log.setTimezone(geoIp.timezone());
            log.setProvedorInternet(geoIp.isp());
            log.setOrganizacao(geoIp.org());
            log.setAsn(geoIp.as());
            log.setProxy(geoIp.proxy());
            log.setHosting(geoIp.hosting());
            log.setMobile(geoIp.mobile());
        } catch (Exception ex) {
            log.setErroGeo(ex.getMessage());
        }
    }

    private String extrairNavegador(String userAgent) {
        String ua = normalizar(userAgent);

        if (ua.contains("edg/")) {
            return "Edge";
        }

        if (ua.contains("opr/") || ua.contains("opera")) {
            return "Opera";
        }

        if (ua.contains("chrome/") || ua.contains("crios/")) {
            return "Chrome";
        }

        if (ua.contains("firefox/") || ua.contains("fxios/")) {
            return "Firefox";
        }

        if (ua.contains("safari/")) {
            return "Safari";
        }

        return "Desconhecido";
    }

    private String extrairSistemaOperacional(String userAgent) {
        String ua = normalizar(userAgent);

        if (ua.contains("windows")) {
            return "Windows";
        }

        if (ua.contains("android")) {
            return "Android";
        }

        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ios")) {
            return "iOS";
        }

        if (ua.contains("mac os") || ua.contains("macintosh")) {
            return "macOS";
        }

        if (ua.contains("linux")) {
            return "Linux";
        }

        return "Desconhecido";
    }

    private String extrairTipoDispositivo(String userAgent) {
        String ua = normalizar(userAgent);

        if (ua.contains("tablet") || ua.contains("ipad")) {
            return "Tablet";
        }

        if (ua.contains("mobile") || ua.contains("android") || ua.contains("iphone")) {
            return "Mobile";
        }

        return "Desktop";
    }

    private Boolean isBot(String userAgent) {
        String ua = normalizar(userAgent);

        return ua.contains("bot")
                || ua.contains("crawler")
                || ua.contains("spider")
                || ua.contains("slurp")
                || ua.contains("curl")
                || ua.contains("wget");
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.toLowerCase(Locale.ROOT);
    }
}
