package dev.kraus.ERP.Config;



import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringConfig {

    private final dev.kraus.ERP.Service.Usuarios.UsuariosService usuariosService;

    public SpringConfig(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
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
                            "/stripe/webhook"
                    ).permitAll();

                    registry.requestMatchers("/api/**", "/stripe/webhook")
                            .permitAll();

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
                                response.sendRedirect("/main");
                            });
                })

                .userDetailsService(usuariosService)

                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return usuariosService.getPasswordEncoder();
    }


}
