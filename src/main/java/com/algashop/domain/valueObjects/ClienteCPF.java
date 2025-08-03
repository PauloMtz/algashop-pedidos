package com.algashop.domain.valueObjects;

import java.util.Objects;

public record ClienteCPF(String cpf) {
    
    public ClienteCPF {
        Objects.requireNonNull(cpf);

        if (cpf.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
	public String toString() {
		return cpf;
	}
}
