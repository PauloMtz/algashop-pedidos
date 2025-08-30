package com.algashop.domain.valueObjects;

import java.util.Objects;

import lombok.Builder;

@Builder
public record InformacoesCobranca(ClienteNome nome, ClienteCPF cpf, ClienteEmail email,
    ClienteTelefone telefone, ClienteEndereco endereco) {

    public InformacoesCobranca {
		Objects.requireNonNull(nome);
		Objects.requireNonNull(cpf);
		Objects.requireNonNull(email);
		Objects.requireNonNull(telefone);
		Objects.requireNonNull(endereco);
	}
}
