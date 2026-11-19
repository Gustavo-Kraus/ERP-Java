package dev.kraus.ERP.Controller.API.APIAuth;

import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Enums.Produtos.TipoProduto;
import dev.kraus.ERP.Model.Enums.Produtos.UnidadeMedida;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import dev.kraus.ERP.Repository.Estoque.EstoqueRepository;
import dev.kraus.ERP.Repository.Filiais.FiliaisRepository;
import dev.kraus.ERP.Repository.Pedidos.PedidoRepository;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.AlmoxarifadoRepository;
import dev.kraus.ERP.Repository.Produtos.ProdutosRepository;
import dev.kraus.ERP.Repository.Tokens.ApiTokenRepository;
import dev.kraus.ERP.Repository.Usuarios.UsuariosRepository;
import dev.kraus.ERP.Service.Tokens.ApiTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class ApiIntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ApiTokenService apiTokenService;

    @Autowired
    protected ApiTokenRepository apiTokenRepository;

    @Autowired
    protected FiliaisRepository filiaisRepository;

    @Autowired
    protected UsuariosRepository usuariosRepository;

    @Autowired
    protected ClientesRepository clientesRepository;

    @Autowired
    protected ProdutosRepository produtosRepository;

    @Autowired
    protected EstoqueRepository estoqueRepository;

    @Autowired
    protected AlmoxarifadoRepository almoxarifadoRepository;

    @Autowired
    protected PedidoRepository pedidoRepository;

    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CacheManager cacheManager;

    protected String adminToken;
    protected Filiais filial;
    protected Produtos produto;
    protected Almoxarifado almoxarifado;
    protected Almoxarifado segundoAlmoxarifado;
    protected Clientes cliente;
    protected Estoque estoque;

    @BeforeEach
    void prepararDadosBase() {
        cacheManager.getCacheNames().forEach(nome -> cacheManager.getCache(nome).clear());

        filial = new Filiais();
        filial.setCodigo("FIL-TESTE");
        filial.setRazaoSocial("Filial de teste");
        filial.setNomeFantasia("Filial teste");
        filial.setCnpj("00000000000191");
        filial = filiaisRepository.save(filial);

        Usuarios admin = criarUsuario("admin@teste.local", "ADMIN");
        adminToken = apiTokenService.gerarToken(admin).getToken();

        almoxarifado = criarAlmoxarifado("Principal");
        segundoAlmoxarifado = criarAlmoxarifado("Secundario");

        produto = new Produtos();
        produto.setCodigo("PROD-BASE");
        produto.setCodigoBarras("7890000000010");
        produto.setNome("Produto de teste");
        produto.setUnidadeMedida(UnidadeMedida.UNIDADE);
        produto.setTipoProduto(TipoProduto.PRODUTO);
        produto.setAtivo(true);
        produto.setCriadoEm(LocalDateTime.now());
        produto = produtosRepository.save(produto);

        cliente = new Clientes();
        cliente.setNome("Cliente base");
        cliente.setEmail("cliente@teste.local");
        cliente = clientesRepository.save(cliente);

        estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setAlmoxarifado(almoxarifado);
        estoque.setQuantidadeAtual(new BigDecimal("10"));
        estoque.setQuantidadeReservada(BigDecimal.ZERO);
        estoque.setPrecoCusto(new BigDecimal("5.00"));
        estoque.setPrecoVenda(new BigDecimal("10.00"));
        estoque = estoqueRepository.save(estoque);
    }

    protected Usuarios criarUsuario(String email, String role) {
        Usuarios usuario = new Usuarios();
        usuario.setNome(email);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode("senha-teste"));
        usuario.setRole(role);
        usuario.setFiliais(filial);
        return usuariosRepository.save(usuario);
    }

    protected Almoxarifado criarAlmoxarifado(String nome) {
        Almoxarifado novo = new Almoxarifado();
        novo.setAlmoxarifado(nome);
        novo.setFiliais(filial);
        return almoxarifadoRepository.save(novo);
    }

    protected String produtoJson(String codigo, String nome) {
        return """
                {"codigo":"%s","codigoBarras":"%s","nome":"%s","unidadeMedida":"UNIDADE","tipoProduto":"PRODUTO"}
                """.formatted(codigo, codigo + "-BAR", nome);
    }

    protected String estoqueJson(Long produtoId, Long almoxarifadoId, String quantidade) {
        return """
                {"produto":%d,"almoxarifado":%d,"quantidadeAtual":%s,"quantidadeReservada":0,"precoCusto":5,"precoVenda":10}
                """.formatted(produtoId, almoxarifadoId, quantidade);
    }

    protected String pedidoJson(Long clienteId, Long estoqueId, String quantidade) {
        return """
                {"clienteId":%d,"itens":[{"estoqueId":%d,"quantidade":%s,"precoVenda":10}]}
                """.formatted(clienteId, estoqueId, quantidade);
    }
}
