package com.algashop.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.algashop.persistence.embeddable.EntregaEmbeddable;
import com.algashop.persistence.embeddable.FaturamentoEmbeddable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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

    /*
     * os dois atributos abaixo seriam suficientes com as anotações embedded
     * porém, as duas classe têm endereço em comum, o que causa conflito (atributos duplicados)
     * por isso, serão necessários atributos identificados, no caso, os attributeoverrides
     */

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "nome", column = @Column(name = "faturamento_nome")),
        @AttributeOverride(name = "sobrenome", column = @Column(name = "faturamento_sobrenome")),
        @AttributeOverride(name = "cpf", column = @Column(name = "faturamento_cpf")),
        @AttributeOverride(name = "email", column = @Column(name = "faturamento_email")),
        @AttributeOverride(name = "telefone", column = @Column(name = "faturamento_telefone")),

        // Endereco do faturamento
        @AttributeOverride(name = "endereco.logradouro", column = @Column(name = "faturamento_logradouro")),
        @AttributeOverride(name = "endereco.complemento", column = @Column(name = "faturamento_complemento")),
        @AttributeOverride(name = "endereco.bairro", column = @Column(name = "faturamento_bairro")),
        @AttributeOverride(name = "endereco.cidade", column = @Column(name = "faturamento_cidade")),
        @AttributeOverride(name = "endereco.estado", column = @Column(name = "faturamento_estado")),
        @AttributeOverride(name = "endereco.cep", column = @Column(name = "faturamento_cep"))
    })
    private FaturamentoEmbeddable faturamento;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "valorEntrega", column = @Column(name = "entrega_valor_entrega")),
        @AttributeOverride(name = "previsaoEntrega", column = @Column(name = "entrega_previsao_entrega")),

        // Cliente (destinatário) da entrega
        @AttributeOverride(name = "cliente.nome", column = @Column(name = "entrega_cliente_nome")),
        @AttributeOverride(name = "cliente.sobrenome", column = @Column(name = "entrega_cliente_sobrenome")),
        @AttributeOverride(name = "cliente.cpf", column = @Column(name = "entrega_cliente_cpf")),
        @AttributeOverride(name = "cliente.telefone", column = @Column(name = "entrega_cliente_telefone")),

        // Endereco da entrega
        @AttributeOverride(name = "endereco.logradouro", column = @Column(name = "entrega_logradouro")),
        @AttributeOverride(name = "endereco.complemento", column = @Column(name = "entrega_complemento")),
        @AttributeOverride(name = "endereco.bairro", column = @Column(name = "entrega_bairro")),
        @AttributeOverride(name = "endereco.cidade", column = @Column(name = "entrega_cidade")),
        @AttributeOverride(name = "endereco.estado", column = @Column(name = "entrega_estado")),
        @AttributeOverride(name = "endereco.cep", column = @Column(name = "entrega_cep"))
    })
    private EntregaEmbeddable entrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemPedidoPersistenceEntity> itens = new HashSet<>();

    @Builder
    public PedidoPersistenceEntity(Long id, UUID clienteId, BigDecimal valorTotal, Integer qtdeTotalItens,
            String status, String formaPagamento, OffsetDateTime criadoEm, OffsetDateTime pagoEm,
            OffsetDateTime canceladoEm, OffsetDateTime finalizadoEm, UUID criadoPorUsuarioID,
            OffsetDateTime ultimaModificacaoEm, UUID ultimaModificacaoPorUsuarioID, Long versaoPedido,
            FaturamentoEmbeddable faturamento, EntregaEmbeddable entrega, Set<ItemPedidoPersistenceEntity> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.valorTotal = valorTotal;
        this.qtdeTotalItens = qtdeTotalItens;
        this.status = status;
        this.formaPagamento = formaPagamento;
        this.criadoEm = criadoEm;
        this.pagoEm = pagoEm;
        this.canceladoEm = canceladoEm;
        this.finalizadoEm = finalizadoEm;
        this.criadoPorUsuarioID = criadoPorUsuarioID;
        this.ultimaModificacaoEm = ultimaModificacaoEm;
        this.ultimaModificacaoPorUsuarioID = ultimaModificacaoPorUsuarioID;
        this.versaoPedido = versaoPedido;
        this.faturamento = faturamento;
        this.entrega = entrega;
        this.substituirItens(itens);
    }

    public void substituirItens(Set<ItemPedidoPersistenceEntity> itens) {
        if (itens == null || itens.isEmpty()) {
            this.setItens(new HashSet<>());
            return;
        }

        itens.forEach(i -> i.setPedido(this));
        this.setItens(itens);
    }

    public void adicionarItem(ItemPedidoPersistenceEntity item) {
        if (item == null) {
            return;
        }

        if (this.getItens() == null) {
            this.setItens(new HashSet<>());
        }

        item.setPedido(this);
        this.getItens().add(item);
    }
}
