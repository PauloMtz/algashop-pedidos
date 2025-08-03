package com.algashop.domain.valueObjects;

import java.util.Objects;

public record ClienteCEP(String cep) {

    public ClienteCEP {
        Objects.requireNonNull(cep);

        if (cep.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (cep.length() != 8) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return cep;
    }
}
