package com.algashop.domain.valueObjects;

import java.util.Objects;

public record ClienteNome(String nome, String sobrenome) {
    
    public ClienteNome(String nome, String sobrenome) {
        Objects.requireNonNull(nome);
        Objects.requireNonNull(sobrenome);

        if (nome.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (sobrenome.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.nome = nome.trim();
        this.sobrenome = sobrenome.trim();
    }

    @Override
    public String toString() {
        return nome + " " + sobrenome;
    }
}
