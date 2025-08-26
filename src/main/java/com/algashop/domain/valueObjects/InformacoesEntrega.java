package com.algashop.domain.valueObjects;

import java.util.Objects;

import lombok.Builder;

@Builder
public record InformacoesEntrega(ClienteNome nome, ClienteCPF cpf, 
    ClienteTelefone telefone, ClienteEndereco endereco) {

    public InformacoesEntrega {
		Objects.requireNonNull(nome);
		Objects.requireNonNull(cpf);
		Objects.requireNonNull(telefone);
		Objects.requireNonNull(endereco);
	}
}
