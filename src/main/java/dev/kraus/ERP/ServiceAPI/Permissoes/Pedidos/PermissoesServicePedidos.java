package dev.kraus.ERP.ServiceAPI.Permissoes.Pedidos;

import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.Model.Permissoes.Pedidos.RestringeAcessoPedidos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Permissoes.RestringeAcessoPedidosRepository;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class PermissoesServicePedidos {

    private final RestringeAcessoPedidosRepository repository;

    public PermissoesServicePedidos(RestringeAcessoPedidosRepository repository) {
        this.repository = repository;
    }

    public void verificarPodeListarPedido(Usuarios usuario) {
        verificar(usuario, RestringeAcessoPedidos::isPodeListarPedido,
                "Usuário não possui permissão para listar pedidos");
    }

    public void verificarPodeCriarPedido(Usuarios usuario) {
        verificar(usuario, RestringeAcessoPedidos::isPodeCriarPedido,
                "Usuário não possui permissão para criar pedidos");
    }

    public void verificarPodeEditarPedido(Usuarios usuario) {
        verificar(usuario, RestringeAcessoPedidos::isPodeEditarPedido,
                "Usuário não possui permissão para editar pedidos");
    }

    public void verificarPodeExcluirPedido(Usuarios usuario) {
        verificar(usuario, RestringeAcessoPedidos::isPodeExcluirPedido,
                "Usuário não possui permissão para excluir pedidos");
    }

    private void verificar(
            Usuarios usuario,
            Predicate<RestringeAcessoPedidos> permissao,
            String mensagem
    ) {
        boolean permitido = usuario != null
                && (isAdmin(usuario)
                || repository.findByUsuario(usuario).filter(permissao).isPresent());
        if (!permitido) {
            throw new GlobalException(mensagem);
        }
    }

    private boolean isAdmin(Usuarios usuario) {
        return usuario.getRole() != null && usuario.getRole().equalsIgnoreCase("ADMIN");
    }
}
