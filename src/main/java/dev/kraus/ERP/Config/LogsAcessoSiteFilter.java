package dev.kraus.ERP.Config;


import dev.kraus.ERP.Service.Logs.LogsAcessoSiteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class LogsAcessoSiteFilter extends OncePerRequestFilter {

    private final LogsAcessoSiteService logsAcessoSiteService;
    private final boolean logsAtivos;
    private final List<String> caminhosIgnorados;

    public LogsAcessoSiteFilter(
            LogsAcessoSiteService logsAcessoSiteService,
            @Value("${app.access-log.enabled:true}") boolean logsAtivos,
            @Value("${app.access-log.ignored-prefixes:/css,/js,/images,/favicon.ico,/error}") List<String> caminhosIgnorados
    ) {
        this.logsAcessoSiteService = logsAcessoSiteService;
        this.logsAtivos = logsAtivos;
        this.caminhosIgnorados = caminhosIgnorados;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!logsAtivos) {
            return true;
        }

        String caminho = request.getRequestURI();

        return caminhosIgnorados
                .stream()
                .anyMatch(caminho::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long inicio = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duracaoMs = System.currentTimeMillis() - inicio;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            logsAcessoSiteService.salvarAcesso(
                    request,
                    response,
                    duracaoMs,
                    authentication
            );
        }
    }
}
