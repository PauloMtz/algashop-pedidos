package com.algashop.persistence.embeddable;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class EntregaEmbeddable {
    private BigDecimal valorEntrega;
    private LocalDate previsaoEntrega;

    @Embedded
	private DestinatarioEmbeddable cliente;
    
    @Embedded
    private EnderecoEmbeddable endereco;
}
