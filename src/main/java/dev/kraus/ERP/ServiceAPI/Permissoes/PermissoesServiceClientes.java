package dev.kraus.ERP.ServiceAPI.Permissoes;

import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.Model.Permissoes.RestringeAcesso;
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
                .map(RestringeAcesso::isPodeListarCliente)
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
                .map(RestringeAcesso::isPodeCadastrarCliente)
                .orElse(false);

        if (!podeCadastrar) {
            throw new GlobalException(
                    "Usuário não possui permissão para cadastrar clientes"
            );
        }
    }
}
