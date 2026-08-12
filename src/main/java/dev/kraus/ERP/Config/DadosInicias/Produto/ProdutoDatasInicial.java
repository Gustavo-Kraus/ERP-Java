package dev.kraus.ERP.Config.DadosInicias.Produto;


import dev.kraus.ERP.Model.Enums.Produtos.TipoProduto;
import dev.kraus.ERP.Model.Enums.Produtos.UnidadeMedida;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Categoria;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Marca;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.CategoriaRepository;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.MarcaRepository;
import dev.kraus.ERP.Repository.Produtos.ProdutosRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ProdutoDatasInicial implements ApplicationRunner {


    private final ProdutosRepository produtosRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoDatasInicial(ProdutosRepository produtosRepository, MarcaRepository marcaRepository, CategoriaRepository categoriaRepository) {
        this.produtosRepository = produtosRepository;
        this.marcaRepository = marcaRepository;
        this.categoriaRepository = categoriaRepository;
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (produtosRepository.count() > 0) {
            System.out.println("Produto padrão já existe.");
            return;
        }
        if (categoriaRepository.count()> 0) {
            System.out.println("Categoria padrão já existe.");
            return;
        }
        if (marcaRepository.count() > 0) {
            System.out.println("Marca padrão já existe.");
            return;
        }
        Categoria categoria = new Categoria();
        categoria.setCategoria("Padrão");
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        System.out.println("Categoria criada. ID: " + categoriaSalva.getId());

        Marca marca = new Marca();
        marca.setMarca("Padrão");
        Marca marcaSalva = marcaRepository.save(marca);
        System.out.println("Marca criada. ID: " + marcaSalva.getId());

        Long codigoincremento = 000000L;
        while (produtosRepository.count() < 50) {
            Produtos produtos = new Produtos();
            produtos.setCategoria(categoriaSalva);
            produtos.setMarca(marcaSalva);
            produtos.setNome("Padrão");
            produtos.setCodigo(String.valueOf(codigoincremento));
            produtos.setTipoProduto(TipoProduto.PRODUTO);
            produtos.setUnidadeMedida(UnidadeMedida.UNIDADE);
            produtos.setDescricao("Produto padrão");
            produtos.setAtivo(true);
            Produtos produtoSalvo = produtosRepository.save(produtos);
            System.out.println("Produto criado. ID: " + produtoSalvo.getId());
            codigoincremento++;
        }

    }
}
