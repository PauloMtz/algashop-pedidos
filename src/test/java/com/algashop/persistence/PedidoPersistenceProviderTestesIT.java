package com.algashop.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.models.Pedido;
import com.algashop.models.testes_pedidos.PedidoTestesDataBuilder;
import com.algashop.persistence.assembler.PedidoPersistenceAssembler;
import com.algashop.persistence.config.SpringDataAuditoriaConfig;
import com.algashop.persistence.disassembler.PedidoPersistenceDisassembler;
import com.algashop.persistence.provider.PedidoPersistenceProvider;
import com.algashop.persistence.repository.PedidoRepository;

@DataJpaTest
@Import({
    PedidoPersistenceProvider.class,
    PedidoPersistenceAssembler.class,
    PedidoPersistenceDisassembler.class,
    SpringDataAuditoriaConfig.class
})
public class PedidoPersistenceProviderTestesIT {
    
    private PedidoPersistenceProvider persistenceProvider;
    private PedidoRepository pedidoRepository;

    @Autowired
    public PedidoPersistenceProviderTestesIT(PedidoPersistenceProvider persistenceProvider,
            PedidoRepository pedidoRepository) {
        this.persistenceProvider = persistenceProvider;
        this.pedidoRepository = pedidoRepository;
    }

    @Test
    public void atualizaManterPersistencia() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        long pedidoId = pedido.getId().valor().toLong();
        persistenceProvider.adicionar(pedido);
        
        var persistenceEntity = pedidoRepository.findById(pedidoId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(PedidoStatus.FEITO.name());
        Assertions.assertThat(persistenceEntity.getCriadoPorUsuarioID()).isNotNull();
        Assertions.assertThat(persistenceEntity.getUltimaModificacaoEm()).isNotNull();
        Assertions.assertThat(persistenceEntity.getUltimaModificacaoPorUsuarioID()).isNotNull();

        pedido = persistenceProvider.buscaId(pedido.getId()).orElseThrow();
        pedido.pedidoPago();
        persistenceProvider.adicionar(pedido);

        persistenceEntity = pedidoRepository.findById(pedidoId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(PedidoStatus.PAGO.name());
        Assertions.assertThat(persistenceEntity.getCriadoPorUsuarioID()).isNotNull();
        Assertions.assertThat(persistenceEntity.getUltimaModificacaoEm()).isNotNull();
        Assertions.assertThat(persistenceEntity.getUltimaModificacaoPorUsuarioID()).isNotNull();
    }
}
