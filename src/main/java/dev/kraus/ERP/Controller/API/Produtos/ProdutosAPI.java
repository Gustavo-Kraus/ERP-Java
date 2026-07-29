package dev.kraus.ERP.Controller.API.Produtos;

import dev.kraus.ERP.DTO.Produtos.ProdutoRequest;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.ServiceAPI.Produtos.ProdutosServiceAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/produtos")
public class ProdutosAPI {

    private final ProdutosServiceAPI produtosServiceApi;

    public ProdutosAPI(ProdutosServiceAPI produtosServiceApi) {
        this.produtosServiceApi = produtosServiceApi;
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarProdutos(
            @RequestParam(value = "busca", required = false) String busca,
            Authentication authentication
    ) {
        return ResponseEntity.ok()
                .body(produtosServiceApi.listarProdutos(busca, getUsuarioAutenticado(authentication)));
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(
            @RequestBody ProdutoRequest request,
            Authentication authentication
    ) {
        Produtos produto = produtosServiceApi.salvarProduto(request, getUsuarioAutenticado(authentication));

        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        produtosServiceApi.deletarProdutos(id, getUsuarioAutenticado(authentication));

        return ResponseEntity.ok().body("Produto apagado com sucesso");
    }

    private Usuarios getUsuarioAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Usuarios usuario) {
            return usuario;
        }

        throw new RuntimeException("Usuario autenticado nao encontrado");
    }
}
