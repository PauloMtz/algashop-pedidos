package com.algashop.domain.exceptions;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.valueObjects.id.PedidoId;

public class PedidoNaoPodeEditarException extends DomainException {

    public PedidoNaoPodeEditarException(PedidoId pedidoId, PedidoStatus status) {
        super(String.format(MensagensErros.ERRO_PEDIDO_NAO_PODE_EDITAR, pedidoId, status));
    }
}
