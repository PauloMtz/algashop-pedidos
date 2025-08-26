package com.algashop.domain.valueObjects;

import com.algashop.domain.validators.ValidacaoCampos;

public record ProdutoNome(String valor) {
    
    public ProdutoNome {
		ValidacaoCampos.requerCampoPreenchido(valor);
	}

	@Override
	public String toString() {
		return valor;
	}
}
