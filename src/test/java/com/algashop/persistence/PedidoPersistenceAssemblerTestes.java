package com.algashop.persistence;

import org.junit.jupiter.api.Test;

import com.algashop.domain.models.Pedido;
import com.algashop.domain.models.PedidoItem;
import com.algashop.models.testes_pedidos.PedidoTestesDataBuilder;
import com.algashop.persistence.assembler.PedidoPersistenceAssembler;
import com.algashop.persistence.entity.ItemPedidoPersistenceEntity;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;

public class PedidoPersistenceAssemblerTestes {
    
    private final PedidoPersistenceAssembler assembler = new PedidoPersistenceAssembler();

    @Test
    void converterParaClasseDomínio() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().build();
        PedidoPersistenceEntity persistenceEntity = assembler.fromDomainEntity(pedido);

        assertThat(persistenceEntity).satisfies(
            p -> assertThat(p.getId()).isEqualTo(pedido.getId().valor().toLong()),
            p -> assertThat(p.getClienteId()).isEqualTo(pedido.getClienteId().valor()),
            p -> assertThat(p.getValorTotal()).isEqualTo(pedido.getValorTotal().valor()),
            p -> assertThat(p.getQtdeTotalItens()).isEqualTo(pedido.getQtdeTotal().valor()),
            p -> assertThat(p.getStatus()).isEqualTo(pedido.getStatusPedido().name()),
            p -> assertThat(p.getFormaPagamento()).isEqualTo(pedido.getFormasPagamento().name()),
            p -> assertThat(p.getCriadoEm()).isEqualTo(pedido.getFeitoEm()),
            p -> assertThat(p.getPagoEm()).isEqualTo(pedido.getPagoEm()),
            p -> assertThat(p.getCanceladoEm()).isEqualTo(pedido.getCanceladoEm()),
            p -> assertThat(p.getFinalizadoEm()).isEqualTo(pedido.getFinalizadoEm())
        );
    }

    @Test
    void pedidoSemItens_deveRemoverPersistenceEntity() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setPedidoTemItens(false).build();
        PedidoPersistenceEntity pedidoPersistenceEntity = PedidoPersistenceTestesDataBuilder.pedidoExistente().build();

        Assertions.assertThat(pedido.getItens()).isEmpty();
        Assertions.assertThat(pedidoPersistenceEntity.getItens()).isNotEmpty();

        assembler.mergeEntity(pedidoPersistenceEntity, pedido);

        Assertions.assertThat(pedidoPersistenceEntity.getItens()).isEmpty();
    }

    @Test
    void pedidoComItens_deveAdicionarNaPersitenceEntity() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setPedidoTemItens(true).build();
        PedidoPersistenceEntity pedidoPersistenceEntity = PedidoPersistenceTestesDataBuilder.pedidoExistente().itens(new HashSet<>()).build();

        Assertions.assertThat(pedido.getItens()).isNotEmpty();
        Assertions.assertThat(pedidoPersistenceEntity.getItens()).isEmpty();

        assembler.mergeEntity(pedidoPersistenceEntity, pedido);

        Assertions.assertThat(pedidoPersistenceEntity.getItens()).isNotEmpty();
        Assertions.assertThat(pedidoPersistenceEntity.getItens().size()).isEqualTo(pedido.getItens().size());
    }

    @Test
    void pedidoComItens_deveRemoverCorretamente() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setPedidoTemItens(true).build();
        
        Assertions.assertThat(pedido.getItens().size()).isEqualTo(2);

        Set<ItemPedidoPersistenceEntity> itensDoPedido = pedido.getItens().stream()
            .map(i -> assembler.aPartirDoDominio(i)).collect(Collectors.toSet());
        
        PedidoPersistenceEntity pedidoPersistenceEntity = PedidoPersistenceTestesDataBuilder.pedidoExistente()
            .itens(itensDoPedido)
            .build();

        PedidoItem itemPedido = pedido.getItens().iterator().next();
        pedido.removerItemPedido(itemPedido.getPedidoItemId());

        assembler.mergeEntity(pedidoPersistenceEntity, pedido);
    }
}
