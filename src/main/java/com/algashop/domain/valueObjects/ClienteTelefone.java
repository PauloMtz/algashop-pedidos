package com.algashop.domain.valueObjects;

import java.util.Objects;

public record ClienteTelefone(String telefone) {
    
    public ClienteTelefone {
        Objects.requireNonNull(telefone);

        if (telefone.isBlank()) {
			throw new IllegalArgumentException();
		}
    }

    @Override
	public String toString() {
		return telefone;
	}
}
