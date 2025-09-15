package com.algashop.persistence;

import org.junit.jupiter.api.Test;

import com.algashop.domain.models.Pedido;
import com.algashop.models.testes_pedidos.PedidoTestesDataBuilder;
import com.algashop.persistence.assembler.PedidoPersistenceAssembler;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

import static org.assertj.core.api.Assertions.*;

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
    void fazerAlgumaCoisa() {}
}
