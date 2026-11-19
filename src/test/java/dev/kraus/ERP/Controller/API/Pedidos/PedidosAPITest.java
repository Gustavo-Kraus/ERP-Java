package dev.kraus.ERP.Controller.API.Pedidos;

import dev.kraus.ERP.Controller.API.APIAuth.ApiIntegrationTestSupport;
import dev.kraus.ERP.Model.Pedidos.Pedido;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PedidosAPITest extends ApiIntegrationTestSupport {

    @Test
    void criaListaEditaEExcluiPedido() throws Exception {
        mockMvc.perform(post("/api/pedidos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoJson(cliente.getId(), estoque.getId(), "2")))
                .andExpect(status().isCreated());

        Pedido pedido = pedidoRepository.findAll().getFirst();
        assertEquals(0, new BigDecimal("20").compareTo(pedido.getTotal()));
        mockMvc.perform(get("/api/pedidos/listar")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pedido.getId()));

        mockMvc.perform(put("/api/pedidos/" + pedido.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoJson(cliente.getId(), estoque.getId(), "3")))
                .andExpect(status().isOk());
        assertEquals(0, new BigDecimal("30").compareTo(
                pedidoRepository.findById(pedido.getId()).orElseThrow().getTotal()));

        mockMvc.perform(delete("/api/pedidos/apagar/" + pedido.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
        assertEquals(0, pedidoRepository.count());
    }

    @Test
    void rejeitaPedidoSemItensQuantidadeInvalidaOuClienteInexistente() throws Exception {
        mockMvc.perform(post("/api/pedidos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"clienteId":%d,"itens":[]}
                                """.formatted(cliente.getId())))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/pedidos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoJson(cliente.getId(), estoque.getId(), "-1")))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/pedidos/salvar")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoJson(999999L, estoque.getId(), "1")))
                .andExpect(status().isBadRequest());
        assertEquals(0, pedidoRepository.count());
    }
}
