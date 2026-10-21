package dev.kraus.ERP.ServiceAPI.Permissoes.Produtos;

import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.Model.Permissoes.Produtos.RestringeAcessoProdutos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Permissoes.RestringeAcessoProdutosRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissoesServiceProdutos {

    private final RestringeAcessoProdutosRepository repository;

    public PermissoesServiceProdutos(RestringeAcessoProdutosRepository repository) {
        this.repository = repository;
    }

    public void verificarPodeListarProduto(Usuarios usuario) {
        verificar(usuario, RestringeAcessoProdutos::isPodeListarProduto,
                "Usuário não possui permissão para listar produtos");
    }

    public void verificarPodeCadastrarProduto(Usuarios usuario) {
        verificar(usuario, RestringeAcessoProdutos::isPodeCadastrarProduto,
                "Usuário não possui permissão para cadastrar produtos");
    }

    public void verificarPodeExcluirProduto(Usuarios usuario) {
        verificar(usuario, RestringeAcessoProdutos::isPodeExcluirProduto,
                "Usuário não possui permissão para excluir produtos");
    }

    private void verificar(
            Usuarios usuario,
            java.util.function.Predicate<RestringeAcessoProdutos> permissao,
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
