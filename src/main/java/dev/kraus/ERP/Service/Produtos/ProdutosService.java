package dev.kraus.ERP.Service.Produtos;

import dev.kraus.ERP.DTO.Produtos.ProdutoRequest;
import dev.kraus.ERP.Mapper.Produtos.ProdutoMapper;
import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Produtos.ProdutosRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final ProdutoMapper mapper;

    public ProdutosService(ProdutosRepository produtosRepository, ProdutoMapper mapper) {
        this.produtosRepository = produtosRepository;
        this.mapper = mapper;
    }

    public Produtos salvarProdutosInterno(Produtos produtos, Usuarios usuarios){
        return produtosRepository.save(produtos);
    }

    public List<Produtos> listarProdutos(String busca){
        Pageable limite = PageRequest.of(0, 6);

        if (busca == null || busca.isBlank()) {
            return produtosRepository.findByDesativadoEmIsNullOrderByCriadoEmDesc(limite);
        }

        return produtosRepository.buscarAtivos(busca.trim(), limite);
    }

    public Produtos salvarProdutoAPI(
            ProdutoRequest request
    ){

        Produtos produto = mapper.toEntity(request);

        if (produtosRepository.existsByCodigoOrCodigoBarras(produto.getCodigo(), produto.getCodigoBarras())) {
            throw new RuntimeException("Produto já cadastrado");
        }

        if (produto.getCodigo() == null || produto.getCodigo().isBlank()) {
            throw new RuntimeException("Código do produto é obrigatório");
        }

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new RuntimeException("Nome do produto é obrigatório");
        }

        if (produto.getUnidadeMedida() == null) {
            throw new RuntimeException("Unidade de medida do produto é obrigatória, sendo elas: UNIDADE, METRO, QUILOGRAMA");
        }

        if (produto.getTipoProduto() == null) {
            throw new RuntimeException("Tipo do produto é obrigatório, sendo elas: PRODUTO, SERVICO, MATERIA_PRIMA, PRODUTO_ACABADO, CONSUMO_INTERNO");
        }

        produto.setAtivo(true);
        produto.setCriadoEm(LocalDateTime.now());

        return produtosRepository.save(produto);
    }

    public Produtos deletarProdutos(Long id){
        Produtos produtos = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtos.setDesativadoEm(java.time.LocalDateTime.now());
        return produtosRepository.save(produtos);
    }
}
