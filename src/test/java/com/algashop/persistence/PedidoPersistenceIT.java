package com.algashop.persistence;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.algashop.domain.utils.IDGenerator;
import com.algashop.persistence.entity.PedidoPersistenceEntity;
import com.algashop.persistence.repository.PedidoRepository;

@SpringBootTest
@Transactional
public class PedidoPersistenceIT {

    private final PedidoRepository pedidoPersistenceRepository;
    
    @Autowired
    public PedidoPersistenceIT(PedidoRepository pedidoPersistenceRepository) {
        this.pedidoPersistenceRepository = pedidoPersistenceRepository;
    }

    @Test
    public void teste() {
        assertTrue("Hello".startsWith("H"));
    }

    @Test
    public void persistirRegistro() {
        long pedidoId = IDGenerator.generateTSID().toLong();

        PedidoPersistenceEntity pedido = PedidoPersistenceEntity.builder()
            .id(pedidoId)
            .clienteId(IDGenerator.generateTimeBaseUuid())
            .qtdeTotalItens(2)
            .valorTotal(new BigDecimal("1000"))
            .formaPagamento("DEBITO")
            .criadoEm(OffsetDateTime.now())
            .build();

        pedidoPersistenceRepository.saveAndFlush(pedido);
        Assertions.assertThat(pedidoPersistenceRepository.existsById(pedidoId)).isTrue();
    }

    // o @Transactional (do springboot) é para que um teste não influencie outro
    // sem ele ao executar todos os testes, esse teste abaixo não passaria, 
    // já que inseriu um registro no teste acima
    @Test
    public void contarRegistros() {
        long contarPedidos = pedidoPersistenceRepository.count();
        Assertions.assertThat(contarPedidos).isZero();
    }
}
