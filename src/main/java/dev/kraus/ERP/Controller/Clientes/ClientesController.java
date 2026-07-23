package dev.kraus.ERP.Controller.Clientes;

import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Service.Clientes.clientesService;
import dev.kraus.ERP.Service.Usuarios.UsuariosService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

    private final clientesService clientesService;
    private final UsuariosService usuariosService;

    public ClientesController(clientesService clientesService, UsuariosService usuariosService) {
        this.clientesService = clientesService;
        this.usuariosService = usuariosService;
    }

    @GetMapping
    public String listarClientes(@RequestParam(required = false) String busca, Model model) {
        model.addAttribute("clientes", clientesService.listar(busca));
        model.addAttribute("busca", busca);
        return "clientes";
    }

    @PostMapping("/salvar")
    public String salvarCliente(Clientes clientes, Authentication authentication, RedirectAttributes redirectAttributes) {
        Usuarios usuario = getUsuarioAutenticado(authentication);
        try {
            clientesService.salvar(clientes, usuario);
            redirectAttributes.addFlashAttribute("success", "Cliente cadastrado com sucesso.");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Clientes clientes, RedirectAttributes redirectAttributes) {
        try {
            clientesService.editar(clientes, id);
            redirectAttributes.addFlashAttribute("success", "Cliente editado com sucesso.");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @PostMapping("/excluir/{id}")
    public String deletarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientesService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Cliente excluido com sucesso.");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
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
