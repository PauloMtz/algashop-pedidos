package com.algashop.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum PedidoStatus {
    
    RASCUNHO,
    FEITO(RASCUNHO),
    PAGO(FEITO),
    PRONTO(PAGO),
    CANCELADO(PRONTO, PAGO, FEITO, RASCUNHO);

    /*PedidoStatus(List<PedidoStatus> statusAnteriores) {
        this.statusAnteriores = statusAnteriores;
    }*/

    PedidoStatus(PedidoStatus... statusAnteriores) {
        this.statusAnteriores = Arrays.asList(statusAnteriores);
    }

    private final List<PedidoStatus> statusAnteriores;

    public boolean podeAlterarStatus(PedidoStatus novoStatus) {
        PedidoStatus statusCorrente = this;
        return novoStatus.statusAnteriores.contains(statusCorrente);
    }

    public boolean naoPodeAlterarStatus(PedidoStatus novoStatus) {
        return !podeAlterarStatus(novoStatus);
    }
}
