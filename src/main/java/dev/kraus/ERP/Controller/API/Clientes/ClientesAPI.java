package dev.kraus.ERP.Controller.API.Clientes;

import dev.kraus.ERP.DTO.Clientes.ClienteRequest;
import dev.kraus.ERP.DTO.Clientes.ClienteResponse;
import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.ServiceAPI.Clientes.ClientesServiceAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientesAPI {

    private final ClientesServiceAPI clientesServiceAPI;

    public ClientesAPI(ClientesServiceAPI clientesServiceAPI) {
        this.clientesServiceAPI = clientesServiceAPI;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClienteResponse>> listarClientes(
            Authentication authentication
    ) {
        List<ClienteResponse> clientesListar =
                clientesServiceAPI.listarClientes(
                        getUsuarioAutenticado(authentication)
                );

        return ResponseEntity.ok(clientesListar);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarClientesPorID(
            @PathVariable Long id,
            @RequestParam(value = "busca", required = false) String busca,
            Authentication authentication
    ) {
        return ResponseEntity.ok()
                .body(clientesServiceAPI.listarClientePorID(id, getUsuarioAutenticado(authentication)));
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(
            @RequestBody ClienteRequest request,
            Authentication authentication
    ) {
        Clientes cliente = clientesServiceAPI.salvarCliente(request, getUsuarioAutenticado(authentication));

        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> deletar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        clientesServiceAPI.deletarCliente(id, getUsuarioAutenticado(authentication));

        return ResponseEntity.ok().body("Cliente Apagado com sucesso" +" " + id);
    }

    @PostMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ClienteRequest request,
            Authentication authentication
    ) {
        Clientes cliente = clientesServiceAPI.atualizarCliente(id, request, getUsuarioAutenticado(authentication));

        return ResponseEntity.ok(cliente);
    }







    private Usuarios getUsuarioAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Usuarios usuario) {
            return usuario;
        }

        throw new RuntimeException("Usuario autenticado nao encontrado");
    }
}
