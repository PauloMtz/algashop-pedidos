package com.algashop.domain.valueObjects;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

import com.algashop.domain.exceptions.MensagensErros;

public record ClienteNascimento(LocalDate data) {

    public ClienteNascimento {
        Objects.requireNonNull(data);
        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(MensagensErros.VALIDACAO_ERRO_NASCIMENTO);
        }
    }

    public Integer idade() {
        return (int) Duration.between(data, LocalDate.now()).toDays();
    }

    @Override
	public String toString() {
		return data.toString();
	}
}
