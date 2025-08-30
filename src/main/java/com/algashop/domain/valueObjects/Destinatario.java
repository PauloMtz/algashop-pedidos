package com.algashop.domain.valueObjects;

import java.util.Objects;

import lombok.Builder;

@Builder
public record Destinatario(ClienteNome nome, ClienteCPF cpf, ClienteTelefone telefone) {

    public Destinatario {
        Objects.requireNonNull(nome);
        Objects.requireNonNull(cpf);
        Objects.requireNonNull(telefone);
    }
}
