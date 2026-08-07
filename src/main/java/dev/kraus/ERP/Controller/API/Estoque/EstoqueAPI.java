package dev.kraus.ERP.Controller.API.Estoque;


import dev.kraus.ERP.DTO.Estoque.EstoqueRequest;
import dev.kraus.ERP.DTO.Estoque.EstoqueRequestAtt;
import dev.kraus.ERP.DTO.Estoque.EstoqueRequestMEA;
import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.ServiceAPI.Estoque.EstoqueServiceAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueAPI {

    private final EstoqueServiceAPI estoqueServiceAPI;

    public EstoqueAPI(EstoqueServiceAPI estoqueServiceAPI) {
        this.estoqueServiceAPI = estoqueServiceAPI;
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(
            @RequestBody EstoqueRequest request,
            Authentication authentication
    ) {
        Estoque estoque = estoqueServiceAPI.salvarEstoque(request, getUsuarioAutenticado(authentication));

        return ResponseEntity.ok(estoque);
    }

    @PostMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody EstoqueRequest request,
            Authentication authentication
    ) {
        Estoque estoqueAtualizado = estoqueServiceAPI.atualizarEstoque(id ,request, getUsuarioAutenticado(authentication));
        return ResponseEntity.ok(estoqueAtualizado);
    }


    @PostMapping("/atualizarQTD")
    public ResponseEntity<?> atualizarEstoque(
            @RequestBody EstoqueRequestAtt request,
            Authentication authentication
    ) {
        Estoque estoqueAtualizado = estoqueServiceAPI.atualizarQuantidadeEstoque(request, getUsuarioAutenticado(authentication));
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferirEstoque(
            @RequestBody EstoqueRequestMEA requestMEA,
            Authentication authentication
    ) {
        Estoque estoqueTransferencia = estoqueServiceAPI.transferenciaEstoque(requestMEA, getUsuarioAutenticado(authentication));
        return ResponseEntity.ok(estoqueTransferencia);
    }



    private Usuarios getUsuarioAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Usuarios usuario) {
            return usuario;
        }

        throw new RuntimeException("Usuario autenticado nao encontrado");
    }
}
