package com.algashop.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.persistence.disassembler.PedidoPersistenceDisassembler;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

public class PedidoPersistenceDisassemblerTestes {
    
    private final PedidoPersistenceDisassembler disassembler = new PedidoPersistenceDisassembler();

    @Test
    void converterDeClasseDomínio() {
        PedidoPersistenceEntity persistenceEntity = PedidoPersistenceTestesDataBuilder.pedidoExistente().build();
        Pedido pedido = disassembler.paraClasseDominio(persistenceEntity);

        Assertions.assertThat(pedido).satisfies(
            p -> assertThat(p.getId()).isEqualTo(new PedidoId(persistenceEntity.getId())),
            p -> assertThat(p.getClienteId()).isEqualTo(new ClienteId(persistenceEntity.getClienteId())),
            p -> assertThat(p.getValorTotal()).isEqualTo(new Moeda(persistenceEntity.getValorTotal())),
            p -> assertThat(p.getQtdeTotal()).isEqualTo(new Quantidade(persistenceEntity.getQtdeTotalItens())),
            p -> assertThat(p.getFeitoEm()).isEqualTo(persistenceEntity.getCriadoEm()),
            p -> assertThat(p.getPagoEm()).isEqualTo(persistenceEntity.getPagoEm()),
            p -> assertThat(p.getCanceladoEm()).isEqualTo(persistenceEntity.getCanceladoEm()),
            p -> assertThat(p.getFinalizadoEm()).isEqualTo(persistenceEntity.getFinalizadoEm()),
            p -> assertThat(p.getStatusPedido()).isEqualTo(PedidoStatus.valueOf(persistenceEntity.getStatus())),
            p -> assertThat(p.getFormasPagamento()).isEqualTo(FormasPagamento.valueOf(persistenceEntity.getFormaPagamento()))
        );
    }
}
