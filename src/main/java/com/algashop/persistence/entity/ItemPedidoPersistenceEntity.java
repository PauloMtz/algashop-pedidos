package com.algashop.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = "id")
@Table(name = "itens_pedido")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedidoPersistenceEntity {
    
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private UUID produtoId;
    private String nomeProduto;
    private BigDecimal preco;
    private Integer quantidade;
    private BigDecimal valorTotal;

    @JoinColumn
    @ManyToOne(optional = false)
    private PedidoPersistenceEntity pedido;

    public Long getPedidoId() {
        if (getPedido() == null) {
            return null;
        }

        return getPedido().getId();
    }
}