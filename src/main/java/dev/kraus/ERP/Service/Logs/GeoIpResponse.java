package dev.kraus.ERP.Service.Logs;

public record GeoIpResponse(
        String status,
        String message,
        String country,
        String countryCode,
        String regionName,
        String city,
        String zip,
        Double lat,
        Double lon,
        String timezone,
        String isp,
        String org,
        String as,
        String query,
        Boolean proxy,
        Boolean hosting,
        Boolean mobile
) {
}
