package dev.kraus.ERP.API.Produtos;


import dev.kraus.ERP.DTO.Produtos.ProdutoRequest;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Service.Produtos.ProdutosService;
import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutosAPI {

    private final ProdutosService produtosService;
    private final UsuariosService usuariosService;

    public ProdutosAPI(
            ProdutosService produtosService,
            UsuariosService usuariosService
    ) {
        this.produtosService = produtosService;
        this.usuariosService = usuariosService;
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarProdutos(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha,
            @RequestParam("busca") String busca
    ) {
        if (!"gu".equals(usuario) || !"123".equals(senha)) {
            return ResponseEntity.status(401).body("Credenciais invalidas");
        }



        return ResponseEntity.ok()
                .body(produtosService.listarProdutos(busca));
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha,
            @RequestBody ProdutoRequest request
    ){
        if (!"gu".equals(usuario) || !"123".equals(senha)) {
            return ResponseEntity.status(401).body("Credenciais invalidas");
        }

        Produtos produto = produtosService.salvarProdutoAPI(request);

        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagar(
            @RequestParam("usuario") String usuario,
            @RequestParam("senha") String senha,
            @PathVariable Long id
    ){
        if (!"gu".equals(usuario) || !"123".equals(senha)) {
            return ResponseEntity.status(401).body("Credenciais invalidas");
        }

        produtosService.deletarProdutos(id);

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
