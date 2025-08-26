package com.algashop.domain.valueObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Moeda(BigDecimal valor) implements Comparable<Moeda> {

    private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public static final Moeda ZERO = new Moeda(BigDecimal.ZERO);

    public Moeda(String valor) {
        this(new BigDecimal(valor));
    }

    public Moeda(BigDecimal valor) {
        Objects.requireNonNull(valor);
        this.valor = valor.setScale(2, roundingMode);
        if (this.valor.signum() == -1) {
            throw new IllegalArgumentException();
        }
    }

    public Moeda multiplicar(Quantidade quantidade) {
        Objects.requireNonNull(quantidade);
        if (quantidade.valor() < 1) {
            throw new IllegalArgumentException();
        }
        BigDecimal multiplied = this.valor.multiply(new BigDecimal(quantidade.valor()));
        return new Moeda(multiplied);
    }

    public Moeda adicionar(Moeda moeda) {
        Objects.requireNonNull(moeda);
        return new Moeda(this.valor.add(moeda.valor));
    }

    public Moeda dividir(Moeda o) {
        return new Moeda(this.valor.divide(o.valor, roundingMode));
    }

    @Override
    public String toString() {
        return valor.toString();
    }

    @Override
    public int compareTo(Moeda obj) {
        return this.valor.compareTo(obj.valor);
    }
}
