package dev.kraus.ERP.Controller.ClienteAPI;

import dev.kraus.ERP.Model.Token.ApiToken;
import dev.kraus.ERP.Repository.Tokens.ApiTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientesAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApiTokenRepository apiTokenRepository;

    @Test
    void deveListarClientes() throws Exception {

        ApiToken apiToken = apiTokenRepository.findById(1L)
                .orElseThrow();

        String token = apiToken.getToken();

        mockMvc.perform(
                        get("/api/clientes/listar")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveCriarClientes() throws Exception {

        ApiToken apiToken = apiTokenRepository.findById(1L)
                .orElseThrow();

        String token = apiToken.getToken();

        mockMvc.perform(
                        post("/api/clientes/salvar")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "nome": "Cliente Teste",
                                "email": "cliente@teste.com"
                            }
                            """)
                )
                .andExpect(status().isOk());
    }
}