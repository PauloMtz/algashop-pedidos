package com.algashop.domain.valueObjects.id;

import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.utils.IDGenerator;

public record ClienteId(UUID valor) {
    
    public ClienteId {
        Objects.requireNonNull(valor);
    }

    public ClienteId() {
        this(IDGenerator.generateTimeBaseUuid());
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
