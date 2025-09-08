package com.algashop.domain.valueObjects.id;

import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.utils.IDGenerator;

public record CarrinhoComprasId(UUID valor) {
    
    public CarrinhoComprasId {
        Objects.requireNonNull(valor);
    }

    public CarrinhoComprasId() {
        this(IDGenerator.generateTimeBaseUuid());
    }

    public CarrinhoComprasId(String valor) {
        this(UUID.fromString(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
