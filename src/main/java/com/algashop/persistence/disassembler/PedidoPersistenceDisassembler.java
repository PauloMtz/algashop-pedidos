package com.algashop.persistence.disassembler;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

@Component
public class PedidoPersistenceDisassembler {
    
    public Pedido paraClasseDominio(PedidoPersistenceEntity persistenceEntity) {
        return Pedido.pedidoExistenteBuilder()
            .pedidoId(new PedidoId(persistenceEntity.getId()))
            .clienteId(new ClienteId(persistenceEntity.getClienteId()))
            .valorTotal(new Moeda(persistenceEntity.getValorTotal()))
            .qtdeTotal(new Quantidade(persistenceEntity.getQtdeTotalItens()))
            .statusPedido(PedidoStatus.valueOf(persistenceEntity.getStatus()))
            .formasPagamento(FormasPagamento.valueOf(persistenceEntity.getFormaPagamento()))
            .feitoEm(persistenceEntity.getCriadoEm())
            .pagoEm(persistenceEntity.getPagoEm())
            .canceladoEm(persistenceEntity.getCanceladoEm())
            .finalizadoEm(persistenceEntity.getFinalizadoEm())
            .itens(new HashSet<>())
            .build();
    }
}
