package com.algashop.domain.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cliente_1 {
    
    private UUID id;
    private String nome;
    private LocalDate nascimento;
    private String email;
    private String telefone;
    private String cpf;
    private Boolean notificacoesPromocoesPermitidas;
    private Boolean arquivado;
    private OffsetDateTime cadastradoEm;
    private OffsetDateTime arquivadoEm;
    private Integer pontosFidelidade;
}
