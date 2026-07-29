package dev.kraus.ERP.Config;



import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringConfig {

    private final dev.kraus.ERP.Service.Usuarios.UsuariosService usuariosService;
    private final ApiTokenAuthenticationFilter apiTokenAuthenticationFilter;

    public SpringConfig(UsuariosService usuariosService, ApiTokenAuthenticationFilter apiTokenAuthenticationFilter) {
        this.usuariosService = usuariosService;
        this.apiTokenAuthenticationFilter = apiTokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/stripe/webhook")
                )

                .authorizeHttpRequests(registry -> {

                    registry.requestMatchers(
                            "/login",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/favicon.ico",
                            "/error",
                            "/oauth2/**",
                            "/login/oauth2/**",
                            "/stripe/webhook"
                    ).permitAll();

                    registry.requestMatchers("/stripe/webhook")
                            .permitAll();

                    registry.requestMatchers("/api/**")
                            .authenticated();

                    registry.anyRequest()
                            .authenticated();
                })

                .formLogin(formLogin -> {

                    formLogin.loginPage("/login")
                            .defaultSuccessUrl("/main", true)
                            .failureUrl("/login?error=true");
                })

                .oauth2Login(oauth2Login -> {

                    oauth2Login.loginPage("/login")
                            .successHandler((request, response, authentication) -> {
                                if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
                                    usuariosService.salvar(oauth2Token);
                                }
                                response.sendRedirect("/main");
                            });
                })

                .userDetailsService(usuariosService)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                request -> request.getRequestURI().startsWith("/api/")
                        )
                )
                .addFilterBefore(apiTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return usuariosService.getPasswordEncoder();
    }


}
