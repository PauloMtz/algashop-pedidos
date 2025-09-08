package com.algashop.domain.valueObjects.id;

import java.util.Objects;
import java.util.UUID;

import com.algashop.domain.utils.IDGenerator;

public record CarrinhoComprasItemId(UUID valor) {
    
    public CarrinhoComprasItemId {
        Objects.requireNonNull(valor);
    }

    public CarrinhoComprasItemId() {
        this(IDGenerator.generateTimeBaseUuid());
    }

    public CarrinhoComprasItemId(String valor) {
        this(UUID.fromString(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
