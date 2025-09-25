package com.algashop.persistence.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinatarioEmbeddable {
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;
}
