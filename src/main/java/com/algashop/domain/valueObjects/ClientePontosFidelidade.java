package com.algashop.domain.valueObjects;

import java.util.Objects;

public record ClientePontosFidelidade(Integer valor) implements Comparable<ClientePontosFidelidade> {
    
    public static final ClientePontosFidelidade ZERO = new ClientePontosFidelidade(0);

    public ClientePontosFidelidade() {
        this(0);
    }

    public ClientePontosFidelidade(Integer valor) {
        Objects.requireNonNull(valor);

        if (valor < 0) {
            throw new IllegalArgumentException();
        }

        this.valor = valor;
    }

    public ClientePontosFidelidade adicionarPontos(Integer valor) {
        return adicionarPontos(new ClientePontosFidelidade(valor));
    }

    public ClientePontosFidelidade adicionarPontos(ClientePontosFidelidade pontos) {
        Objects.requireNonNull(pontos);

        if (pontos.valor() < 0) {
            throw new IllegalArgumentException();
        }

        return new ClientePontosFidelidade(this.valor() + pontos.valor());
    }

    @Override
    public String toString() {
        return valor.toString();
    }

    // método da interface Comparable
    @Override
    public int compareTo(ClientePontosFidelidade obj) {
        return this.valor().compareTo(obj.valor());
    }
}
