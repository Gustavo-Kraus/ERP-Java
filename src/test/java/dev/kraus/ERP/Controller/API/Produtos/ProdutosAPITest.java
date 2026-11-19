package dev.kraus.ERP.Controller.API.Produtos;

import dev.kraus.ERP.Controller.API.APIAuth.ApiIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProdutosAPITest extends ApiIntegrationTestSupport {

    @Test
    void criaListaConsultaEExcluiProduto() throws Exception {
        MvcResult criado = mockMvc.perform(post("/api/produtos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJson("PROD-NOVO", "Produto novo")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("PROD-NOVO"))
                .andReturn();
        Number produtoId = com.jayway.jsonpath.JsonPath.read(
                criado.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get("/api/produtos/listar")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].codigo", hasItem("PROD-NOVO")));
        mockMvc.perform(get("/api/produtos/listar/" + produtoId.longValue())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto novo"));

        mockMvc.perform(delete("/api/produtos/apagar/" + produtoId.longValue())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/produtos/listar")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].codigo", not(hasItem("PROD-NOVO"))));
    }

    @Test
    void rejeitaProdutoDuplicadoIncompletoOuComEnumInvalido() throws Exception {
        mockMvc.perform(post("/api/produtos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJson("PROD-BASE", "Codigo duplicado")))
                .andExpect(status().isConflict());
        mockMvc.perform(post("/api/produtos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"PROD-SEM-NOME","unidadeMedida":"UNIDADE","tipoProduto":"PRODUTO"}
                                """))
                .andExpect(status().isConflict());
        mockMvc.perform(post("/api/produtos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"PROD-ENUM-INVALIDO","nome":"Enum invalido","unidadeMedida":"PACOTE","tipoProduto":"PRODUTO"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
