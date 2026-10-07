package dev.kraus.ERP.ServiceAPI.Permissoes;

import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.Model.Permissoes.Clientes.RestringeAcessoClientes;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Permissoes.RestringeAcessoRepository;
import org.springframework.stereotype.Service;


@Service
public class PermissoesServiceClientes {

    private final RestringeAcessoRepository repository;

    public PermissoesServiceClientes(RestringeAcessoRepository repository) {
        this.repository = repository;
    }

    public void verificarPodeListarCliente(Usuarios usuario) {

        boolean podeListar = repository
                .findByUsuario(usuario)
                .map(RestringeAcessoClientes::isPodeListarCliente)
                .orElse(false);

        if (!podeListar) {
            throw new GlobalException(
                    "Usuário não possui permissão para listar clientes"
            );
        }
    }

    public void verificarPodeCadastrarCliente(Usuarios usuario) {

        boolean podeCadastrar = repository
                .findByUsuario(usuario)
                .map(RestringeAcessoClientes::isPodeCadastrarCliente)
                .orElse(false);

        if (!podeCadastrar) {
            throw new GlobalException(
                    "Usuário não possui permissão para cadastrar clientes"
            );
        }
    }

    public void verificarPodeExcluirCliente(Usuarios usuarios){
        boolean podeExcluir = repository
                .findByUsuario(usuarios)
                .map(RestringeAcessoClientes::isPodeExcluirCliente)
                .orElse(false);

        if (!podeExcluir) {
            throw new GlobalException(
                    "Usuário não possui permissão para excluir clientes"
            );
        }
    }

    public void verificarPodeEditarCliente(Usuarios usuarios){
        boolean podeEditar = repository
                .findByUsuario(usuarios)
                .map(RestringeAcessoClientes::isPodeEditarCliente)
                .orElse(false);

        if (!podeEditar) {
            throw new GlobalException(
                    "Usuário não possui permissão para editar clientes"
            );
        }
    }
}
