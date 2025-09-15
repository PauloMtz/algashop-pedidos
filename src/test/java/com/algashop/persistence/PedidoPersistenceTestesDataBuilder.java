package com.algashop.persistence;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.algashop.domain.utils.IDGenerator;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

public class PedidoPersistenceTestesDataBuilder {
    
    private PedidoPersistenceTestesDataBuilder() {}

    public static PedidoPersistenceEntity.PedidoPersistenceEntityBuilder pedidoExistente() {
        return PedidoPersistenceEntity.builder()
            .id(IDGenerator.generateTSID().toLong())
            .clienteId(IDGenerator.generateTimeBaseUuid())
            .qtdeTotalItens(2)
            .valorTotal(new BigDecimal("1000"))
            .status("RASCUNHO")
            .formaPagamento("DEBITO")
            .criadoEm(OffsetDateTime.now());
    }
}
