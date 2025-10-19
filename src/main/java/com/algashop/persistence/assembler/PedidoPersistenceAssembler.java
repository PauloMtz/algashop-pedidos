package com.algashop.persistence.assembler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.algashop.domain.models.Pedido;
import com.algashop.domain.models.PedidoItem;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.Destinatario;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.persistence.embeddable.DestinatarioEmbeddable;
import com.algashop.persistence.embeddable.EnderecoEmbeddable;
import com.algashop.persistence.embeddable.EntregaEmbeddable;
import com.algashop.persistence.embeddable.FaturamentoEmbeddable;
import com.algashop.persistence.entity.ItemPedidoPersistenceEntity;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

@Component
public class PedidoPersistenceAssembler {
    
    public PedidoPersistenceEntity fromDomainEntity(Pedido pedido) {
        return mergeEntity(new PedidoPersistenceEntity(), pedido);
    }

    public PedidoPersistenceEntity mergeEntity(PedidoPersistenceEntity persistenceEntity, Pedido pedido) {
        
        persistenceEntity.setId(pedido.getId().valor().toLong());
        persistenceEntity.setClienteId(pedido.getClienteId().valor());
        persistenceEntity.setValorTotal(pedido.getValorTotal().valor());
        persistenceEntity.setQtdeTotalItens(pedido.getQtdeTotal().valor());
        persistenceEntity.setStatus(pedido.getStatusPedido().name());
        persistenceEntity.setFormaPagamento(pedido.getFormasPagamento().name());
        persistenceEntity.setCriadoEm(pedido.getFeitoEm());
        persistenceEntity.setPagoEm(pedido.getPagoEm());
        persistenceEntity.setCanceladoEm(pedido.getCanceladoEm());
        persistenceEntity.setFinalizadoEm(pedido.getFinalizadoEm());
        persistenceEntity.setVersaoPedido(pedido.getVersaoPedido());
        persistenceEntity.setEntrega(paraEntregaEmbeddable(pedido.getEntrega()));
        persistenceEntity.setFaturamento(paraFaturamentoEmbeddable(pedido.getFaturamento()));
        Set<ItemPedidoPersistenceEntity> mergeItens = mergeItens(pedido, persistenceEntity);
        persistenceEntity.substituirItens(mergeItens);
        return persistenceEntity;
    }

    private EntregaEmbeddable paraEntregaEmbeddable(InformacoesEntrega entrega) {
        if (entrega == null) {
            return null;
        }

        var builder = EntregaEmbeddable.builder()
            .previsaoEntrega(entrega.previsaoEntrega())
            .valorEntrega(entrega.valorEntrega().valor())
            .endereco(paraEnderecoEmbeddable(entrega.endereco()));

        Destinatario cliente = entrega.cliente();

        if (cliente != null) {
            builder.cliente(
                DestinatarioEmbeddable.builder()
                    .nome(cliente.nome().nome())
                    .sobrenome(cliente.nome().sobrenome())
                    .cpf(cliente.cpf().toString())
                    .telefone(cliente.telefone().toString())
                    .build()
            );
        }

        return builder.build();
    }

    private EnderecoEmbeddable paraEnderecoEmbeddable(ClienteEndereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoEmbeddable.builder()
            .cep(endereco.cep().toString())
            .logradouro(endereco.logradouro())
            .complemento(endereco.complemento())
            .bairro(endereco.bairro())
            .cidade(endereco.cidade())
            .estado(endereco.estado())
            .build();
    }

    private FaturamentoEmbeddable paraFaturamentoEmbeddable(InformacoesCobranca cobranca) {
        if (cobranca == null) {
            return null;
        }

        return FaturamentoEmbeddable.builder()
            .nome(cobranca.nome().nome())
            .sobrenome(cobranca.nome().sobrenome())
            .cpf(cobranca.cpf().toString())
            .telefone(cobranca.telefone().toString())
            .endereco(paraEnderecoEmbeddable(cobranca.endereco()))
            .build();
    }

    private Set<ItemPedidoPersistenceEntity> mergeItens(Pedido pedido, PedidoPersistenceEntity persistenceEntity) {
       Set<PedidoItem> itensNovosOuAtualizados = pedido.getItens();

       if (itensNovosOuAtualizados == null || itensNovosOuAtualizados.isEmpty()) {
        return new HashSet<>();
       }

       Set<ItemPedidoPersistenceEntity> itensExistentes = persistenceEntity.getItens();

       if (itensExistentes == null || itensExistentes.isEmpty()) {
        return itensNovosOuAtualizados.stream()
            .map(itemPedido -> aPartirDoDominio(itemPedido))
            .collect(Collectors.toSet());
       }

       Map<Long, ItemPedidoPersistenceEntity> itensExistentesMap = itensExistentes.stream().collect(Collectors.toMap(ItemPedidoPersistenceEntity::getId, itemPedido -> itemPedido));

       /*return itensNovosOuAtualizados.stream()
        .map(itemPedido -> {
            ItemPedidoPersistenceEntity itemPersistenceEntity = itensExistentesMap.getOrDefault(
                itemPedido.getPedidoItemId().valor().toLong(), new ItemPedidoPersistenceEntity()
            );

            return mergeEntity(persistenceEntity, pedido);
        }).collect(Collectors.toSet());*/

        // sugestão ChatGpt
        Set<ItemPedidoPersistenceEntity> itensAtualizados = itensNovosOuAtualizados.stream()
            .map(itemPedido -> {
                ItemPedidoPersistenceEntity existente =
                    itensExistentesMap.getOrDefault(itemPedido.getPedidoItemId().valor().toLong(), new ItemPedidoPersistenceEntity());
                ItemPedidoPersistenceEntity atualizado = mergeItemPedidoEntity(existente, itemPedido);
                atualizado.setPedido(persistenceEntity);
                return atualizado;
            })
            .collect(Collectors.toSet());

        return itensAtualizados;
    }

    public ItemPedidoPersistenceEntity aPartirDoDominio(PedidoItem itemPedido) {
        return mergeItemPedidoEntity(new ItemPedidoPersistenceEntity(), itemPedido);
    }

    private ItemPedidoPersistenceEntity mergeItemPedidoEntity(ItemPedidoPersistenceEntity itemPedidoPersistenceEntity, PedidoItem itemPedido) {
        itemPedidoPersistenceEntity.setId(itemPedido.getPedidoItemId().valor().toLong());
        itemPedidoPersistenceEntity.setProdutoId(itemPedido.getProdutoId().valor());
        itemPedidoPersistenceEntity.setNomeProduto(itemPedido.getProdutoNome().valor());
        itemPedidoPersistenceEntity.setPreco(itemPedido.getPreco().valor());
        itemPedidoPersistenceEntity.setQuantidade(itemPedido.getQtde().valor());
        itemPedidoPersistenceEntity.setValorTotal(itemPedido.getValorTotal().valor());
        return itemPedidoPersistenceEntity;
    }
}
