package dev.kraus.ERP.Repository.Pedidos;

import dev.kraus.ERP.Model.Pedidos.Pedido;
import dev.kraus.ERP.Model.Pedidos.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByNumero(Long numero);

    Optional<Pedido> findTopByOrderByNumeroDesc();

    List<Pedido> findAllByFilialIdOrderByDataCriacaoDesc(Long filialId);

    Optional<Pedido> findByIdAndFilialId(Long id, Long filialId);

    boolean existsByNumero(Long numero);

    boolean existsByClienteIdAndStatus(
            Long clienteId,
            StatusPedido status
    );
}