package com.algashop.domain.valueObjects;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Builder;

// esse toBuilder permite converter esse value object em outro já com as propriedades populadas
@Builder(toBuilder = true)
public record InformacoesEntrega(Moeda valorEntrega, LocalDate previsaoEntrega, 
	Destinatario cliente, ClienteEndereco endereco) {

    public InformacoesEntrega {
		Objects.requireNonNull(valorEntrega);
		Objects.requireNonNull(previsaoEntrega);
		Objects.requireNonNull(cliente);
		Objects.requireNonNull(endereco);
	}
}
