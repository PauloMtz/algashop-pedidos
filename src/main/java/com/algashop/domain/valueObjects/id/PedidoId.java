package com.algashop.domain.valueObjects.id;

import java.util.Objects;

import com.algashop.domain.utils.IDGenerator;

import io.hypersistence.tsid.TSID;

public record PedidoId(TSID valor) {

    // construtores
    public PedidoId {
        Objects.requireNonNull(valor);
    }

    public PedidoId() {
        this(IDGenerator.generateTSID());
    }

    public PedidoId(Long valor) {
        this(TSID.from(valor));
    }

    public PedidoId(String valor) {
        this(TSID.from(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
