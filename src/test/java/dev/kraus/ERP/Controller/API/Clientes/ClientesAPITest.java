package dev.kraus.ERP.Controller.API.Clientes;

import dev.kraus.ERP.Controller.API.APIAuth.ApiIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClientesAPITest extends ApiIntegrationTestSupport {

    @Test
    void criaListaAtualizaEExcluiClienteERejeitaNomeVazio() throws Exception {
        var criado = mockMvc.perform(post("/api/clientes/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Cliente novo","email":"novo@teste.local"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cliente novo"))
                .andReturn();
        Number clienteId = com.jayway.jsonpath.JsonPath.read(
                criado.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get("/api/clientes/listar")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nome", hasItem("Cliente novo")));
        mockMvc.perform(get("/api/clientes/listar/" + clienteId.longValue())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("novo@teste.local"));

        mockMvc.perform(post("/api/clientes/atualizar/" + clienteId.longValue())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Cliente atualizado","email":"atualizado@teste.local"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cliente atualizado"));

        mockMvc.perform(post("/api/clientes/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":" "}
                                """))
                .andExpect(status().isConflict());
        mockMvc.perform(delete("/api/clientes/apagar/" + clienteId.longValue())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
        assertNotNull(clientesRepository.findById(clienteId.longValue()).orElseThrow().getExcluidoEm());
    }
}
