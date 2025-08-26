package com.algashop.domain.exceptions;

import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.domain.valueObjects.id.PedidoItemId;

public class PedidoNaoContemItemException extends DomainException {
    
    public PedidoNaoContemItemException(PedidoId pedidoId, PedidoItemId itemId) {
        super(String.format(MensagensErros.ERRO_PEDIDO_NAO_TEM_ITEM, pedidoId, itemId));
    }
}
