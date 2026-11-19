package dev.kraus.ERP.Controller.API.APIAuth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiTokenAPITest extends ApiIntegrationTestSupport {

    @Test
    void validaCredenciaisECriaTokenComExpiracao() throws Exception {
        var usuario = criarUsuario("token@teste.local", "USER");
        usuario.setSenha(passwordEncoder.encode("senha-correta"));
        usuariosRepository.save(usuario);

        mockMvc.perform(post("/api/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"token@teste.local","senha":"errada","duracao":"DIAS_30"}
                                """))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"token@teste.local","senha":"senha-correta"}
                                """))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"token@teste.local","senha":"senha-correta","duracao":"DIAS_30"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.expiraEm").isNotEmpty());
    }
}
