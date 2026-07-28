package dev.kraus.ERP.Controller.Produtos;

import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Service.Produtos.ProdutosService;
import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutosService produtosService;
    private final UsuariosService usuariosService;

    public ProdutosController(ProdutosService produtosService, UsuariosService usuariosService) {
        this.produtosService = produtosService;
        this.usuariosService = usuariosService;
    }


    @GetMapping
    public String listarProdutos(@RequestParam(required = false) String busca, Model model) {
        model.addAttribute("produtos", produtosService.listarProdutos(busca));
        model.addAttribute("busca", busca);
        return "produtos";
    }

    @PostMapping("/salvar")
    public String salvarProduto(Produtos produtos, Authentication authentication, RedirectAttributes redirectAttributes) {
        Usuarios usuario = getUsuarioAutenticado(authentication);
        try {
            produtosService.salvarProdutosInterno(produtos, usuario);
            redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso.");
            return "redirect:/produtos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/produtos";
        }
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
