package com.algashop.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pedidos")
public class PedidoPersistenceEntity {
    
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private UUID clienteId;
    private BigDecimal valorTotal;
    private Integer qtdeTotalItens;
    private String status;
    private String formaPagamento;
    private OffsetDateTime criadoEm;
    private OffsetDateTime pagoEm;
    private OffsetDateTime canceladoEm;
    private OffsetDateTime finalizadoEm;
}
