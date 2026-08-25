package dev.kraus.ERP.ServiceAPI.Pedidos;

import dev.kraus.ERP.DTO.Pedidos.PedidoItemRequest;
import dev.kraus.ERP.DTO.Pedidos.PedidoItemResponse;
import dev.kraus.ERP.DTO.Pedidos.PedidoRequest;
import dev.kraus.ERP.DTO.Pedidos.PedidoResponse;
import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Estoque.Estoque;
import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Model.Pedidos.Pedido;
import dev.kraus.ERP.Model.Pedidos.StatusPedido;
import dev.kraus.ERP.Model.PedidosItem.PedidoItem;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import dev.kraus.ERP.Repository.Estoque.EstoqueRepository;
import dev.kraus.ERP.Repository.Pedidos.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClientesRepository clientesRepository;
    private final EstoqueRepository estoqueRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClientesRepository clientesRepository,
            EstoqueRepository estoqueRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.clientesRepository = clientesRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public Pedido criarPedido(
            PedidoRequest request,
            Usuarios usuario
    ) {
        Filiais filial = usuario.getFiliais();

        if (filial == null) {
            throw new IllegalStateException(
                    "Usuário não possui filial vinculada."
            );
        }

        Clientes cliente = buscarCliente(request.clienteId());
        Pedido pedido = new Pedido();

        pedido.setUsuario(usuario);
        pedido.setFilial(filial);
        pedido.setCliente(cliente);
        pedido.setNumero(proximoNumero());
        pedido.setStatus(StatusPedido.RASCUNHO);

        pedido.setDesconto(
                request.desconto() != null
                        ? request.desconto()
                        : BigDecimal.ZERO
        );

        pedido.setAcrescimo(
                request.acrescimo() != null
                        ? request.acrescimo()
                        : BigDecimal.ZERO
        );

        pedido.setObservacao(request.observacao());
        adicionarItens(pedido, request);
        calcularTotais(pedido);
        return pedidoRepository.save(pedido);
    }

    public List<PedidoResponse> listarPedidos(Usuarios usuario) {
        validarUsuario(usuario);
        return pedidoRepository.findAllByFilialIdOrderByDataCriacaoDesc(usuario.getFiliais().getId())
                .stream()
                .map(this::paraResponse)
                .toList();
    }

    @Transactional
    public PedidoResponse editarPedido(Long id, PedidoRequest request, Usuarios usuario) {
        validarUsuario(usuario);
        Pedido pedido = buscarPedidoDaFilial(id, usuario);

        pedido.setCliente(buscarCliente(request.clienteId()));
        pedido.setDesconto(valorOuZero(request.desconto()));
        pedido.setAcrescimo(valorOuZero(request.acrescimo()));
        pedido.setObservacao(request.observacao());
        pedido.getItens().clear();
        adicionarItens(pedido, request);
        calcularTotais(pedido);

        return paraResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public void excluirPedido(Long id, Usuarios usuario) {
        validarUsuario(usuario);
        pedidoRepository.delete(buscarPedidoDaFilial(id, usuario));
    }

    private void adicionarItens(Pedido pedido, PedidoRequest request) {
        if (request.itens() == null || request.itens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve possuir ao menos um item.");
        }

        for (PedidoItemRequest itemRequest : request.itens()) {
            Estoque estoque = buscarEstoque(itemRequest.estoqueId());
            validarQuantidade(itemRequest.quantidade());

            BigDecimal preco = itemRequest.precoVenda() != null
                    ? itemRequest.precoVenda()
                    : valorOuZero(estoque.getPrecoVenda());
            validarPreco(preco);

            PedidoItem item = new PedidoItem();
            item.setPedido(pedido);
            item.setEstoque(estoque);
            item.setProduto(estoque.getProduto());
            item.setQuantidade(itemRequest.quantidade());
            item.setValorUnitario(preco);
            item.setDesconto(valorOuZero(itemRequest.desconto()));
            item.setAcrescimo(BigDecimal.ZERO);

            BigDecimal total = preco.multiply(itemRequest.quantidade())
                    .subtract(item.getDesconto());
            if (total.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("O total de um item não pode ser negativo.");
            }

            item.setTotal(total);
            pedido.getItens().add(item);
        }
    }

    private void calcularTotais(Pedido pedido) {

        BigDecimal subtotal = pedido.getItens()
                .stream()
                .map(PedidoItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setSubtotal(subtotal);

        BigDecimal total = subtotal
                .subtract(pedido.getDesconto())
                .add(pedido.getAcrescimo());

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "O total do pedido não pode ser negativo."
            );
        }

        pedido.setTotal(total);
    }

    private void validarQuantidade(BigDecimal quantidade) {

        if (quantidade == null ||
                quantidade.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }
    }

    private Clientes buscarCliente(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O cliente é obrigatório.");
        }
        return clientesRepository.findByIdAndExcluidoEmIsNull(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
    }

    private Estoque buscarEstoque(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O estoque é obrigatório.");
        }
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado."));
    }

    private Long proximoNumero() {
        return pedidoRepository.findTopByOrderByNumeroDesc()
                .map(pedido -> pedido.getNumero() + 1)
                .orElse(1L);
    }

    private BigDecimal valorOuZero(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço de venda não pode ser negativo.");
        }
    }

    private Pedido buscarPedidoDaFilial(Long id, Usuarios usuario) {
        if (id == null) {
            throw new IllegalArgumentException("O id do pedido é obrigatório.");
        }
        return pedidoRepository.findByIdAndFilialId(id, usuario.getFiliais().getId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
    }

    private void validarUsuario(Usuarios usuario) {
        if (usuario == null || !usuario.isEnabled() || usuario.getFiliais() == null) {
            throw new IllegalStateException("Usuário autenticado inválido.");
        }
    }

    private PedidoResponse paraResponse(Pedido pedido) {
        List<PedidoItemResponse> itens = pedido.getItens().stream()
                .map(item -> new PedidoItemResponse(
                        item.getEstoque() != null ? item.getEstoque().getId() : null,
                        item.getProduto().getId(),
                        item.getQuantidade(),
                        item.getValorUnitario(),
                        item.getDesconto(),
                        item.getTotal()
                ))
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getNumero(),
                pedido.getCliente().getId(),
                pedido.getStatus(),
                pedido.getSubtotal(),
                pedido.getDesconto(),
                pedido.getAcrescimo(),
                pedido.getTotal(),
                pedido.getObservacao(),
                pedido.getDataCriacao(),
                pedido.getDataAtualizacao(),
                itens
        );
    }
}
