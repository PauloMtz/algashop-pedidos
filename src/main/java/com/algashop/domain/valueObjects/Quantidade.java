package com.algashop.domain.valueObjects;

import java.io.Serializable;
import java.util.Objects;

public record Quantidade(Integer valor) implements Serializable, Comparable<Quantidade> {
    
    public static final Quantidade ZERO = new Quantidade(0);

	public Quantidade {
		Objects.requireNonNull(valor);
		if (valor < 0) {
			throw new IllegalArgumentException();
		}
	}

    public Quantidade add(Quantidade quantidade) {
		Objects.requireNonNull(quantidade);
		return new Quantidade(this.valor + quantidade.valor());
	}

    @Override
	public String toString() {
		return String.valueOf(valor);
	}

	@Override
	public int compareTo(Quantidade o) {
		return this.valor.compareTo(o.valor);
	}
}
