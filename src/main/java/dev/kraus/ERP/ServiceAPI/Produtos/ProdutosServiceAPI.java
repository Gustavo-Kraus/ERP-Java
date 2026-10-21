package dev.kraus.ERP.ServiceAPI.Produtos;

import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.DTO.Produtos.ProdutoRequest;
import dev.kraus.ERP.Mapper.Produtos.ProdutoMapper;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Produtos.ProdutosRepository;
import dev.kraus.ERP.ServiceAPI.Permissoes.Produtos.PermissoesServiceProdutos;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutosServiceAPI {

    private final ProdutosRepository produtosRepository;
    private final ProdutoMapper mapper;
    private final PermissoesServiceProdutos permissoesServiceProdutos;

    public ProdutosServiceAPI(
            ProdutosRepository produtosRepository,
            ProdutoMapper mapper,
            PermissoesServiceProdutos permissoesServiceProdutos
    ) {
        this.produtosRepository = produtosRepository;
        this.mapper = mapper;
        this.permissoesServiceProdutos = permissoesServiceProdutos;
    }

    public List<Produtos> listarProdutos(String busca, Usuarios usuario) {
        permissoesServiceProdutos.verificarPodeListarProduto(usuario);
        validarUsuario(usuario);

        Pageable limite = PageRequest.of(0, 6);

        if (busca == null || busca.isBlank()) {
            return produtosRepository.findByDesativadoEmIsNullOrderByCriadoEmDesc(limite);
        }

        return produtosRepository.buscarAtivos(busca.trim(), limite);
    }

    public Produtos salvarProduto(ProdutoRequest request, Usuarios usuario) {
        permissoesServiceProdutos.verificarPodeCadastrarProduto(usuario);
        validarUsuario(usuario);

        Produtos produto = mapper.toEntity(request);

        if (produtosRepository.existsByCodigoOrCodigoBarras(produto.getCodigo(), produto.getCodigoBarras())) {
            throw new GlobalException("Produto já cadastrado");
        }

        if (produto.getCodigo() == null || produto.getCodigo().isBlank()) {
            throw new GlobalException("Codigo do produto e obrigatorio");
        }

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new GlobalException("Nome do produto e obrigatorio");
        }

        if (produto.getUnidadeMedida() == null) {
            throw new GlobalException("Unidade de medida do produto e obrigatoria, sendo elas: UNIDADE, METRO, QUILOGRAMA");
        }

        if (produto.getTipoProduto() == null) {
            throw new GlobalException("Tipo do produto e obrigatorio, sendo elas: PRODUTO, SERVICO, MATERIA_PRIMA, PRODUTO_ACABADO, CONSUMO_INTERNO");
        }

        produto.setAtivo(true);
        produto.setCriadoEm(LocalDateTime.now());

        return produtosRepository.save(produto);
    }

    public Produtos deletarProdutos(Long id, Usuarios usuario) {
        permissoesServiceProdutos.verificarPodeExcluirProduto(usuario);
        validarUsuario(usuario);

        Produtos produtos = produtosRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Produto nao encontrado"));

        produtos.setDesativadoEm(LocalDateTime.now());
        return produtosRepository.save(produtos);
    }

    public Produtos listarProdutoPorID(Long id, Usuarios usuario) {
        permissoesServiceProdutos.verificarPodeListarProduto(usuario);
        validarUsuario(usuario);

        return produtosRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Produto nao encontrado"));
    }


    private void validarUsuario(Usuarios usuario) {
        if (usuario == null || !usuario.isEnabled()) {
            throw new GlobalException("Usuario autenticado invalido");
        }
    }
}
