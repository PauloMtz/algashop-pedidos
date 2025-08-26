package com.algashop.domain.exceptions;

import com.algashop.domain.valueObjects.id.PedidoId;

public class DataEntregaInvalidaException extends DomainException {

    public DataEntregaInvalidaException(PedidoId pedidoId) {
        super(String.format(MensagensErros.ERRO_DATA_ENTREGA_INVALIDA, pedidoId));
    }
}
