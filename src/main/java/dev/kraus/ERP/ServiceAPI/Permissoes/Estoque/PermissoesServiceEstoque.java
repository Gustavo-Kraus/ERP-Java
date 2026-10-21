package dev.kraus.ERP.ServiceAPI.Permissoes.Estoque;

import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.Model.Permissoes.Estoque.RestringeAcessoEstoque;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Permissoes.RestringeAcessoEstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class PermissoesServiceEstoque {

    private final RestringeAcessoEstoqueRepository repository;

    public PermissoesServiceEstoque(RestringeAcessoEstoqueRepository repository) {
        this.repository = repository;
    }

    public void verificarPodeListarEstoque(Usuarios usuario) {
        verificar(usuario, RestringeAcessoEstoque::isPodeListarEstoque,
                "Usuário não possui permissão para listar estoque");
    }

    public void verificarPodeCadastrarEstoque(Usuarios usuario) {
        verificar(usuario, RestringeAcessoEstoque::isPodeCadastrarEstoque,
                "Usuário não possui permissão para cadastrar estoque");
    }

    public void verificarPodeAtualizarEstoque(Usuarios usuario) {
        verificar(usuario, RestringeAcessoEstoque::isPodeAtualizarEstoque,
                "Usuário não possui permissão para atualizar estoque");
    }

    public void verificarPodeAtualizarQuantidadeEstoque(Usuarios usuario) {
        verificar(usuario, RestringeAcessoEstoque::isPodeAtualizarQuantidadeEstoque,
                "Usuário não possui permissão para atualizar quantidade do estoque");
    }

    public void verificarPodeTransferirEstoque(Usuarios usuario) {
        verificar(usuario, RestringeAcessoEstoque::isPodeTransferirEstoque,
                "Usuário não possui permissão para transferir estoque");
    }

    private void verificar(
            Usuarios usuario,
            Predicate<RestringeAcessoEstoque> permissao,
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
