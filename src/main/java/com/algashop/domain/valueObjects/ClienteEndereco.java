package com.algashop.domain.valueObjects;

import java.util.Objects;

import com.algashop.domain.validators.ValidacaoCampos;

import lombok.Builder;

@Builder(toBuilder = true)
public record ClienteEndereco(String logradouro, String complemento, String bairro,
    String cidade, String estado, ClienteCEP cep ) {
      
    public ClienteEndereco {
        ValidacaoCampos.requerCampoPreenchido(logradouro);
        ValidacaoCampos.requerCampoPreenchido(bairro);
        ValidacaoCampos.requerCampoPreenchido(cidade);
        ValidacaoCampos.requerCampoPreenchido(estado);
        Objects.requireNonNull(cep);
    }
}
