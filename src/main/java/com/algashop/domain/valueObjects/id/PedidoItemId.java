package com.algashop.domain.valueObjects.id;

import java.util.Objects;

import com.algashop.domain.utils.IDGenerator;

import io.hypersistence.tsid.TSID;

public record PedidoItemId(TSID valor) {

    // construtores
    public PedidoItemId {
        Objects.requireNonNull(valor);
    }

    public PedidoItemId() {
        this(IDGenerator.generateTSID());
    }

    public PedidoItemId(Long valor) {
        this(TSID.from(valor));
    }

    public PedidoItemId(String valor) {
        this(TSID.from(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
