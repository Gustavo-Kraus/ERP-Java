package dev.kraus.ERP.Controller.API.Clientes;

import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.ServiceAPI.Clientes.ClientesServiceAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClientesAPI {

    private final ClientesServiceAPI clientesServiceAPI;

    public ClientesAPI(ClientesServiceAPI clientesServiceAPI) {
        this.clientesServiceAPI = clientesServiceAPI;
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarClientes(
            @RequestParam(value = "busca", required = false) String busca,
            Authentication authentication
    ) {
        return ResponseEntity.ok()
                .body(clientesServiceAPI.listarClientes(busca, getUsuarioAutenticado(authentication)));
    }






    private Usuarios getUsuarioAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Usuarios usuario) {
            return usuario;
        }

        throw new RuntimeException("Usuario autenticado nao encontrado");
    }
}
