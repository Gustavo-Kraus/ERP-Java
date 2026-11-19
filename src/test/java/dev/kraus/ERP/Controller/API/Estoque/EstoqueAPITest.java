package dev.kraus.ERP.Controller.API.Estoque;

import dev.kraus.ERP.Controller.API.APIAuth.ApiIntegrationTestSupport;
import dev.kraus.ERP.Model.Estoque.Estoque;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EstoqueAPITest extends ApiIntegrationTestSupport {

    @Test
    void cadastraEListaEstoqueERejeitaDuplicidade() throws Exception {
        mockMvc.perform(post("/api/estoque/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estoqueJson(produto.getId(), almoxarifado.getId(), "10")))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/estoque/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estoqueJson(produto.getId(), segundoAlmoxarifado.getId(), "2")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/estoque/listar")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void atualizaQuantidadeEValidaTransferenciasSemEfeitosParciais() throws Exception {
        mockMvc.perform(post("/api/estoque/atualizar/" + estoque.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estoqueJson(produto.getId(), almoxarifado.getId(), "12")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/estoque/atualizarqtd")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":%d,"quantidade":1,"metodo":"DESCONHECIDO"}
                                """.formatted(estoque.getId())))
                .andExpect(status().isConflict());
        mockMvc.perform(post("/api/estoque/atualizarqtd")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":%d,"quantidade":2,"metodo":"ENTRADA"}
                                """.formatted(estoque.getId())))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/estoque/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estoqueJson(produto.getId(), segundoAlmoxarifado.getId(), "2")))
                .andExpect(status().isOk());
        Estoque destino = estoqueRepository
                .findByProdutoIdAndAlmoxarifadoId(produto.getId(), segundoAlmoxarifado.getId())
                .orElseThrow();

        transferir(estoque.getId(), destino.getId(), "-1")
                .andExpect(status().isConflict());
        transferir(estoque.getId(), destino.getId(), "99")
                .andExpect(status().isConflict());
        assertQuantidade(estoque.getId(), "14.000");
        assertQuantidade(destino.getId(), "2.000");

        transferir(estoque.getId(), destino.getId(), "3")
                .andExpect(status().isOk());
        assertQuantidade(estoque.getId(), "11.000");
        assertQuantidade(destino.getId(), "5.000");
    }

    private org.springframework.test.web.servlet.ResultActions transferir(
            Long origemId,
            Long destinoId,
            String quantidade
    ) throws Exception {
        return mockMvc.perform(post("/api/estoque/transferencia")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"idEstoqueOrigem":%d,"idEstoqueDestino":%d,"quantidade":%s}
                        """.formatted(origemId, destinoId, quantidade)));
    }

    private void assertQuantidade(Long estoqueId, String quantidade) {
        assertEquals(0, new BigDecimal(quantidade).compareTo(
                estoqueRepository.findById(estoqueId).orElseThrow().getQuantidadeAtual()));
    }
}
