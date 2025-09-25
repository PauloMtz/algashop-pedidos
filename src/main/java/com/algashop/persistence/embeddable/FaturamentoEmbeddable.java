package com.algashop.persistence.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaturamentoEmbeddable {
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String telefone;

    @Embedded
    private EnderecoEmbeddable endereco;
}
