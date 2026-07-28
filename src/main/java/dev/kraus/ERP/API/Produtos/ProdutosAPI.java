package dev.kraus.ERP.API.Produtos;


import dev.kraus.ERP.DTO.Produtos.ProdutoRequest;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Service.Produtos.ProdutosServiceAPI;
import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutosAPI {

    private final ProdutosServiceAPI produtosServiceApi;
    private final UsuariosService usuariosService;

    public ProdutosAPI(ProdutosServiceAPI produtosServiceApi, UsuariosService usuariosService) {
        this.produtosServiceApi = produtosServiceApi;
        this.usuariosService = usuariosService;
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarProdutos(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha,
            @RequestParam("busca") String busca
    ) {
        return ResponseEntity.ok()
                .body(produtosServiceApi.listarProdutos(busca, usuario, senha));
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(
            @RequestBody ProdutoRequest request
    ){
        Produtos produto = produtosServiceApi.salvarProduto(request);

        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagar(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha,
            @PathVariable Long id
    ){
        produtosServiceApi.deletarProdutos(id, usuario, senha);

        return ResponseEntity.ok().body("Produto apagado com sucesso");
    }





    private Usuarios getUsuarioAutenticado(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            return usuariosService.salvar(token);
        }
        if (authentication != null && authentication.getPrincipal() instanceof Usuarios usuario) {
            return usuario;
        }

        throw new RuntimeException("Usuario autenticado nao encontrado");
    }
}
