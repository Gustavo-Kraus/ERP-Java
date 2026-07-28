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


    public Produtos deletarProdutos(Long id){
        Produtos produtos = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtos.setDesativadoEm(java.time.LocalDateTime.now());
        return produtosRepository.save(produtos);
    }
}
