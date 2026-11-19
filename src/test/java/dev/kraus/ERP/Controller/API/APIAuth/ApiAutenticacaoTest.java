package dev.kraus.ERP.Controller.API.APIAuth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
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
class ApiAutenticacaoTest extends ApiIntegrationTestSupport {

    @Test
    void exigeAutenticacaoNasRotasDeLeituraEGravacao() throws Exception {
        long clientesAntes = clientesRepository.count();
        long produtosAntes = produtosRepository.count();
        long estoquesAntes = estoqueRepository.count();
        long pedidosAntes = pedidoRepository.count();

        mockMvc.perform(get("/api/clientes/listar")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/produtos/listar")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/estoque/listar")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/pedidos/listar")).andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/clientes/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Nao autorizado"}
                                """))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/produtos/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJson("PROD-NAO-AUTORIZADO", "Nao autorizado")))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/estoque/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estoqueJson(produto.getId(), almoxarifado.getId(), "5")))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/pedidos/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoJson(cliente.getId(), estoque.getId(), "1")))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/clientes/atualizar/" + cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Nao autorizado"}
                                """))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/clientes/apagar/" + cliente.getId()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/produtos/apagar/" + produto.getId()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/estoque/atualizar/" + estoque.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(estoqueJson(produto.getId(), almoxarifado.getId(), "1")))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/estoque/atualizarqtd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":%d,"quantidade":1,"metodo":"ENTRADA"}
                                """.formatted(estoque.getId())))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/estoque/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idEstoqueOrigem":%d,"idEstoqueDestino":%d,"quantidade":1}
                                """.formatted(estoque.getId(), estoque.getId())))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/pedidos/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoJson(cliente.getId(), estoque.getId(), "1")))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/pedidos/apagar/999999"))
                .andExpect(status().isUnauthorized());

        assertEquals(clientesAntes, clientesRepository.count());
        assertEquals(produtosAntes, produtosRepository.count());
        assertEquals(estoquesAntes, estoqueRepository.count());
        assertEquals(pedidosAntes, pedidoRepository.count());
    }

    @Test
    void rejeitaTokenInvalidoRevogadoExpiradoEDeUsuarioDesabilitado() throws Exception {
        mockMvc.perform(get("/api/produtos/listar")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());

        var usuarioRevogado = criarUsuario("revogado@teste.local", "ADMIN");
        var tokenRevogado = apiTokenService.gerarToken(usuarioRevogado);
        tokenRevogado.setRevogadoEm(LocalDateTime.now());
        apiTokenRepository.save(tokenRevogado);
        mockMvc.perform(get("/api/estoque/listar")
                        .header("X-API-Token", tokenRevogado.getToken()))
                .andExpect(status().isUnauthorized());

        var usuarioExpirado = criarUsuario("expirado@teste.local", "ADMIN");
        String tokenExpirado = apiTokenService
                .gerarToken(usuarioExpirado, LocalDateTime.now().minusMinutes(1))
                .getToken();
        mockMvc.perform(get("/api/pedidos/listar")
                        .header("Authorization", "Bearer " + tokenExpirado))
                .andExpect(status().isUnauthorized());

        var usuarioDesabilitado = criarUsuario("desabilitado@teste.local", "ADMIN");
        usuarioDesabilitado.setExcluidoEm(LocalDateTime.now());
        usuariosRepository.save(usuarioDesabilitado);
        String tokenDesabilitado = apiTokenService.gerarToken(usuarioDesabilitado).getToken();
        mockMvc.perform(get("/api/clientes/listar")
                        .header("Authorization", "Bearer " + tokenDesabilitado))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void aceitaCabecalhoAlternativoMasRespeitaPermissoesDoUsuario() throws Exception {
        mockMvc.perform(get("/api/produtos/listar")
                        .header("X-API-Token", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].codigo", hasItem("PROD-BASE")));

        var usuario = criarUsuario("sem-permissao@teste.local", "USER");
        String token = apiTokenService.gerarToken(usuario).getToken();
        mockMvc.perform(get("/api/produtos/listar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.erro", containsString("permissão")));
        mockMvc.perform(get("/api/estoque/listar").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/pedidos/listar").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/clientes/listar").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        long produtosAntes = produtosRepository.count();
        mockMvc.perform(post("/api/produtos/salvar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJson("PROD-NEGADO", "Produto negado")))
                .andExpect(status().isForbidden());
        assertEquals(produtosAntes, produtosRepository.count());
    }
}
