package com.algashop.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

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
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedBy
    private UUID criadoPorUsuarioID;

    @LastModifiedDate
    private OffsetDateTime ultimaModificacaoEm;

    @LastModifiedBy
    private UUID ultimaModificacaoPorUsuarioID;

    @Version
    private Long versaoPedido;
}
