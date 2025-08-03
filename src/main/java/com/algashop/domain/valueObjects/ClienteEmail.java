package com.algashop.domain.valueObjects;

import java.util.Objects;

import com.algashop.domain.validators.ValidacaoCampos;

public record ClienteEmail(String email) {
    
    public ClienteEmail {
		Objects.requireNonNull(email);
		ValidacaoCampos.requerEmailValido(email);
	}

	@Override
	public String toString() {
		return email;
	}
}
