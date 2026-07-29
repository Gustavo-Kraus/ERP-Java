package dev.kraus.ERP.Controller.Tokens;

import dev.kraus.ERP.Model.Token.ApiToken;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Service.Tokens.ApiTokenService;
import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tokens")
public class ApiTokensController {

    private final ApiTokenService apiTokenService;
    private final UsuariosService usuariosService;

    public ApiTokensController(ApiTokenService apiTokenService, UsuariosService usuariosService) {
        this.apiTokenService = apiTokenService;
        this.usuariosService = usuariosService;
    }

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        Usuarios usuario = getUsuarioAutenticado(authentication);
        model.addAttribute("tokens", apiTokenService.listarTokens(usuario));
        return "tokens";
    }

    @PostMapping("/gerar")
    public String gerar(Authentication authentication, RedirectAttributes redirectAttributes) {
        Usuarios usuario = getUsuarioAutenticado(authentication);
        ApiToken token = apiTokenService.gerarToken(usuario);
        redirectAttributes.addFlashAttribute("tokenCriado", token.getToken());
        return "redirect:/tokens";
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
