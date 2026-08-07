package dev.kraus.ERP.ServiceAPI.Estoque;


import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.DTO.Estoque.EstoqueRequest;
import dev.kraus.ERP.DTO.Estoque.EstoqueRequestAtt;
import dev.kraus.ERP.DTO.Estoque.EstoqueRequestMEA;
import dev.kraus.ERP.Model.Enums.Produtos.TipoMovimentacao;
import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Estoque.EstoqueMovimentacao;
import dev.kraus.ERP.Model.Produtos.Dimensoes.Almoxarifado;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Estoque.EstoqueMovimentacaoRepository;
import dev.kraus.ERP.Repository.Estoque.EstoqueRepository;
import dev.kraus.ERP.Repository.Produtos.Dimensoes.AlmoxarifadoRepository;
import dev.kraus.ERP.Repository.Produtos.ProdutosRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalDateTime;

@Service
public class EstoqueServiceAPI {

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMovimentacaoRepository estoqueMovimentacaoRepository;
    private final ProdutosRepository produtosRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;

    public EstoqueServiceAPI(EstoqueRepository estoqueRepository, EstoqueMovimentacaoRepository estoqueMovimentacaoRepository, ProdutosRepository produtosRepository, AlmoxarifadoRepository almoxarifadoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.estoqueMovimentacaoRepository = estoqueMovimentacaoRepository;
        this.produtosRepository = produtosRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
    }

    public Estoque salvarEstoque(EstoqueRequest request, Usuarios usuarios) {
        validarUsuario(usuarios);

        if (request.getProduto() == null) {
            throw new GlobalException("Id do produto é obrigatório");
        }

        Produtos produto = produtosRepository.findById(request.getProduto())
                .orElseThrow(() -> new GlobalException("Produto não encontrado"));

        Almoxarifado almoxarifado = almoxarifadoRepository.findById(request.getAlmoxarifado())
                .orElseThrow(() -> new GlobalException("Almoxarifado não encontrado"));

        Estoque estoque = new Estoque();

        estoque.setProduto(produto);
        estoque.setAlmoxarifado(almoxarifado);
        estoque.setQuantidadeAtual(request.getQuantidadeAtual());
        estoque.setQuantidadeReservada(request.getQuantidadeReservada());
        estoque.setEstoqueMaximo(request.getEstoqueMaximo());
        estoque.setEstoqueMinimo(request.getEstoqueMinimo());
        estoque.setPontoReposicao(request.getPontoReposicao());
        estoque.setAtualizadoEm(LocalDateTime.now());
        return estoqueRepository.save(estoque);
    }

    public Estoque atualizarEstoque(Long id ,EstoqueRequest request, Usuarios usuarios){
        validarUsuario(usuarios);
        EstoqueMovimentacao movimentacao = new EstoqueMovimentacao();

        return estoqueRepository.findById(id)
                .map(estoque -> {
                    movimentacao.setDataMovimentacao(LocalDateTime.now());
                    movimentacao.setProduto(estoque.getProduto());
                    movimentacao.setQuantidade(request.getQuantidadeAtual());
                    movimentacao.setTipo(TipoMovimentacao.ATUALIZACAO);
                    movimentacao.setDestino(estoque.getAlmoxarifado());
                    movimentacao.setUsuario(usuarios);
                    movimentacao.setOrigem(estoque.getAlmoxarifado());
                    estoqueMovimentacaoRepository.save(movimentacao);

                    estoque.setQuantidadeAtual(request.getQuantidadeAtual());
                    estoque.setQuantidadeReservada(request.getQuantidadeReservada());
                    estoque.setEstoqueMaximo(request.getEstoqueMaximo());
                    estoque.setEstoqueMinimo(request.getEstoqueMinimo());
                    estoque.setPontoReposicao(request.getPontoReposicao());
                    estoque.setAtualizadoEm(LocalDateTime.now());

                    return estoqueRepository.save(estoque);
                })
                .orElseThrow(() -> new GlobalException("Estoque não encontrado"));
    }

    public Estoque atualizarQuantidadeEstoque(EstoqueRequestAtt requestAtt, Usuarios usuarios) {
        validarUsuario(usuarios);

        EstoqueMovimentacao movimentacao = new EstoqueMovimentacao();

        Estoque estoque = estoqueRepository.findById(requestAtt.getId())
                .orElseThrow(() -> new GlobalException("Estoque não encontrado"));


        if (requestAtt.getMetodo().equalsIgnoreCase("ENTRADA")) {
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual().add(requestAtt.getQuantidade()));
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            movimentacao.setProduto(estoque.getProduto());
            movimentacao.setQuantidade(requestAtt.getQuantidade());
            movimentacao.setTipo(TipoMovimentacao.ENTRADA);
            movimentacao.setDestino(estoque.getAlmoxarifado());
            movimentacao.setUsuario(usuarios);
            movimentacao.setOrigem(estoque.getAlmoxarifado());
            estoqueMovimentacaoRepository.save(movimentacao);
        } else if (requestAtt.getMetodo().equalsIgnoreCase("SAIDA")) {
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual().subtract(requestAtt.getQuantidade()));
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            movimentacao.setProduto(estoque.getProduto());
            movimentacao.setQuantidade(requestAtt.getQuantidade());
            movimentacao.setTipo(TipoMovimentacao.SAIDA);
            movimentacao.setDestino(estoque.getAlmoxarifado());
            movimentacao.setUsuario(usuarios);
            movimentacao.setOrigem(estoque.getAlmoxarifado());
            estoqueMovimentacaoRepository.save(movimentacao);
        } else {
            throw new GlobalException("Método inválido. Use 'ENTRADA' ou 'SAIDA'.");
        }

        return estoqueRepository.save(estoque);
    }


    //mandar de um almoxarifado para o outro
    @Transactional
    public Estoque transferenciaEstoque(EstoqueRequestMEA requestMEA, Usuarios usuario) {
        validarUsuario(usuario);

        Estoque estoqueOrigem = estoqueRepository.findById(requestMEA.getIdEstoqueOrigem())
                .orElseThrow(() -> new GlobalException("Estoque de origem não encontrado"));

        Estoque estoqueDestino = estoqueRepository.findById(requestMEA.getIdEstoqueDestino())
                .orElseThrow(() -> new GlobalException("Estoque de destino não encontrado"));

        if (!estoqueOrigem.getProduto().getId().equals(estoqueDestino.getProduto().getId())) {
            throw new GlobalException("A transferência só pode ocorrer entre estoques do mesmo produto.");
        }

        if (requestMEA.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new GlobalException("A quantidade deve ser maior que zero.");
        }

        if (requestMEA.getQuantidade().compareTo(estoqueOrigem.getQuantidadeAtual()) > 0) {
            throw new GlobalException("Quantidade insuficiente em estoque.");
        }

        estoqueOrigem.setQuantidadeAtual(
                estoqueOrigem.getQuantidadeAtual().subtract(requestMEA.getQuantidade()));

        estoqueDestino.setQuantidadeAtual(
                estoqueDestino.getQuantidadeAtual().add(requestMEA.getQuantidade()));

        EstoqueMovimentacao movimentacao = new EstoqueMovimentacao();
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setProduto(estoqueOrigem.getProduto());
        movimentacao.setQuantidade(requestMEA.getQuantidade());
        movimentacao.setTipo(TipoMovimentacao.TRANSFERENCIA);
        movimentacao.setOrigem(estoqueOrigem.getAlmoxarifado());
        movimentacao.setDestino(estoqueDestino.getAlmoxarifado());
        movimentacao.setUsuario(usuario);
        movimentacao.setDocumento("Foi feito uma tranferencia do almoxarifado "
                + estoqueOrigem.getAlmoxarifado().getAlmoxarifado()
                + " para o almoxarifado "
                + estoqueDestino.getAlmoxarifado().getAlmoxarifado()
                + " do produto "
                + estoqueOrigem.getProduto().getNome()
                + " com a quantidade de "
                + requestMEA.getQuantidade());

        estoqueRepository.save(estoqueOrigem);
        estoqueRepository.save(estoqueDestino);
        estoqueMovimentacaoRepository.save(movimentacao);

        return estoqueDestino;
    }


    private void validarUsuario(Usuarios usuario) {
        if (usuario == null || !usuario.isEnabled()) {
            throw new GlobalException("Usuario autenticado invalido");
        }
    }

}