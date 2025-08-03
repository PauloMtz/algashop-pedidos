package com.algashop.domain.valueObjects;

import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.utils.UUIDGenerator;

public record ClienteId(UUID value) {
    
    public ClienteId() {
        this(UUIDGenerator.generateTimeBaseUuid());
    }

    public ClienteId(UUID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
