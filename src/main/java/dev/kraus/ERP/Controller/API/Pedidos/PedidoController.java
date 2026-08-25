package dev.kraus.ERP.Controller.API.Pedidos;

import dev.kraus.ERP.DTO.Pedidos.PedidoRequest;
import dev.kraus.ERP.DTO.Pedidos.PedidoResponse;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.ServiceAPI.Pedidos.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> criar(
            @RequestBody PedidoRequest request,
            Authentication authentication
    ) {

        Usuarios usuario = getUsuarioAutenticado(authentication);
        Long pedidoId = pedidoService.criarPedido(request, usuario).getId();

        return ResponseEntity.status(HttpStatus.CREATED).body("Pedido criado com sucesso. ID: " + pedidoId);
    }

    @GetMapping({"", "/listar"})
    public ResponseEntity<List<PedidoResponse>> listar(Authentication authentication) {
        return ResponseEntity.ok(
                pedidoService.listarPedidos(getUsuarioAutenticado(authentication))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> editar(
            @PathVariable Long id,
            @RequestBody PedidoRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                pedidoService.editarPedido(
                        id,
                        request,
                        getUsuarioAutenticado(authentication)
                )
        );
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            Authentication authentication
    ) {
        pedidoService.excluirPedido(id, getUsuarioAutenticado(authentication));
        return ResponseEntity.noContent().build();
    }

    private Usuarios getUsuarioAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Usuarios usuario) {
            return usuario;
        }
        throw new IllegalStateException("Usuário autenticado não encontrado.");
    }
}