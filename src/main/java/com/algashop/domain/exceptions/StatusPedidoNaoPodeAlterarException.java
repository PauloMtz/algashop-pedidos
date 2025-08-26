package com.algashop.domain.exceptions;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.valueObjects.id.PedidoId;

import static com.algashop.domain.exceptions.MensagensErros.ERRO_ALTERAR_STATUS_PEDIDO;

public class StatusPedidoNaoPodeAlterarException extends DomainException {

    public StatusPedidoNaoPodeAlterarException(PedidoId id, PedidoStatus status, PedidoStatus novoStatus) {
        super(String.format(ERRO_ALTERAR_STATUS_PEDIDO, id, status, novoStatus));
    }
}
