package dev.kraus.ERP.Repository.Pedidos;


import dev.kraus.ERP.Model.PedidosItem.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}