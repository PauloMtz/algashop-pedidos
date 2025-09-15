package com.algashop.persistence.assembler;

import org.springframework.stereotype.Component;

import com.algashop.domain.models.Pedido;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

@Component
public class PedidoPersistenceAssembler {
    
    public PedidoPersistenceEntity fromDomainEntity(Pedido pedido) {
        return mergEntity(new PedidoPersistenceEntity(), pedido);
    }

    public PedidoPersistenceEntity mergEntity(PedidoPersistenceEntity persistenceEntity, Pedido pedido) {
        
        persistenceEntity.setId(pedido.getId().valor().toLong());
        persistenceEntity.setClienteId(pedido.getClienteId().valor());
        persistenceEntity.setValorTotal(pedido.getValorTotal().valor());
        persistenceEntity.setQtdeTotalItens(pedido.getQtdeTotal().valor());
        persistenceEntity.setStatus(pedido.getStatusPedido().name());
        persistenceEntity.setFormaPagamento(pedido.getFormasPagamento().name());
        persistenceEntity.setCriadoEm(pedido.getFeitoEm());
        persistenceEntity.setPagoEm(pedido.getPagoEm());
        persistenceEntity.setCanceladoEm(pedido.getCanceladoEm());
        persistenceEntity.setFinalizadoEm(pedido.getFinalizadoEm());
        
        return persistenceEntity;
    }
}
