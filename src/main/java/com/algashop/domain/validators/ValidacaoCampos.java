package com.algashop.domain.validators;

import java.util.Objects;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidacaoCampos {
    
    // construtor privado
    private ValidacaoCampos() {}

    public static void requerEmailValido(String email) {
        requerEmailValido(email, null);
    }

    public static void requerEmailValido(String email, String mensagemErro) {
        Objects.requireNonNull(email, mensagemErro);

        if (email.isBlank()) {
            throw new IllegalArgumentException(mensagemErro);
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }
}
