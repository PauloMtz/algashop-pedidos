package com.algashop.persistence;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

import com.algashop.domain.utils.IDGenerator;
import com.algashop.persistence.entity.ItemPedidoPersistenceEntity;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

public class PedidoPersistenceTestesDataBuilder {
    
    private PedidoPersistenceTestesDataBuilder() {}

    public static PedidoPersistenceEntity.PedidoPersistenceEntityBuilder pedidoExistente() {
        return PedidoPersistenceEntity.builder()
            .id(IDGenerator.generateTSID().toLong())
            .clienteId(IDGenerator.generateTimeBaseUuid())
            .qtdeTotalItens(3)
            .valorTotal(new BigDecimal("5000"))
            .status("RASCUNHO")
            .formaPagamento("DEBITO")
            .criadoEm(OffsetDateTime.now())
            .itens(Set.of(
                itemExistente().build(),
                itemAlternativo().build()
            ));
    }

    public static ItemPedidoPersistenceEntity.ItemPedidoPersistenceEntityBuilder itemExistente() {
        return ItemPedidoPersistenceEntity.builder()
            .id(IDGenerator.generateTSID().toLong())
            .preco(new BigDecimal(2000))
            .quantidade(2)
            .valorTotal(new BigDecimal(4000))
            .nomeProduto("Notebook Teste I")
            .produtoId(IDGenerator.generateTimeBaseUuid());
    }

    public static ItemPedidoPersistenceEntity.ItemPedidoPersistenceEntityBuilder itemAlternativo() {
        return ItemPedidoPersistenceEntity.builder()
            .id(IDGenerator.generateTSID().toLong())
            .preco(new BigDecimal(1000))
            .quantidade(1)
            .valorTotal(new BigDecimal(1000))
            .nomeProduto("Mouse para Notebook Teste")
            .produtoId(IDGenerator.generateTimeBaseUuid());
    }
}
