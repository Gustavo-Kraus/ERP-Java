package dev.kraus.ERP.Service.Logs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeoIpService {

    private final RestClient restClient;
    private final boolean geoAtivo;

    public GeoIpService(
            RestClient.Builder restClientBuilder,
            @Value("${app.access-log.geo-url:http://ip-api.com/json}") String geoUrl,
            @Value("${app.access-log.geo-enabled:true}") boolean geoAtivo
    ) {
        this.restClient = restClientBuilder.baseUrl(geoUrl).build();
        this.geoAtivo = geoAtivo;
    }

    public GeoIpResponse buscar(String ip) {

        if (!geoAtivo || ip == null || ip.isBlank() || isIpLocal(ip)) {
            return null;
        }

        return restClient
                .get()
                .uri("/{ip}?fields=status,message,country,countryCode,regionName,city,zip,lat,lon,timezone,isp,org,as,query,proxy,hosting,mobile", ip)
                .retrieve()
                .body(GeoIpResponse.class);
    }

    private boolean isIpLocal(String ip) {
        return ip.equals("127.0.0.1")
                || ip.equals("0:0:0:0:0:0:0:1")
                || ip.equals("::1")
                || ip.startsWith("10.")
                || ip.startsWith("192.168.")
                || ip.matches("^172\\.(1[6-9]|2[0-9]|3[0-1])\\..*");
    }
}
