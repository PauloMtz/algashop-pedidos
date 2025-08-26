package com.algashop.domain.valueObjects.id;

import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.utils.IDGenerator;

public record ProdutoId(UUID valor) {
    
    public ProdutoId {
        Objects.requireNonNull(valor);
    }

    public ProdutoId() {
        this(IDGenerator.generateTimeBaseUuid());
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
