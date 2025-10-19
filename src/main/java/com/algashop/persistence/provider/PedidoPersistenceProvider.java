package com.algashop.persistence.provider;

import java.lang.reflect.Field;
import java.util.Optional;

import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algashop.domain.models.Pedido;
import com.algashop.domain.repository.PedidoRepositorio;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.persistence.assembler.PedidoPersistenceAssembler;
import com.algashop.persistence.disassembler.PedidoPersistenceDisassembler;
import com.algashop.persistence.entity.PedidoPersistenceEntity;
import com.algashop.persistence.repository.PedidoRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoPersistenceProvider implements PedidoRepositorio {

    private final PedidoRepository pedidoRepository;
    private final PedidoPersistenceAssembler assembler;
    private final PedidoPersistenceDisassembler disassembler;

    private final EntityManager entityManager;

    @Override
    public Optional<Pedido> buscaId(PedidoId id) {
        Optional<PedidoPersistenceEntity> entidade = pedidoRepository.findById(id.valor().toLong());;
        return entidade.map(disassembler::paraClasseDominio);
    }

    @Override
    public boolean existente(PedidoId id) {
        return pedidoRepository.existsById(id.valor().toLong());
    }

    @Override
    @Transactional(readOnly = false)
    public void adicionar(Pedido aggregateRoot) {
        long pedidoId = aggregateRoot.getId().valor().toLong();

        pedidoRepository.findById(pedidoId).ifPresentOrElse(
            (persistenceEntity) -> {
                atualizar(aggregateRoot, persistenceEntity);
            }, 
            () -> {
                inserir(aggregateRoot);
            }
        );

        /*PedidoPersistenceEntity persistenceEntity = assembler.fromDomainEntity(aggregateRoot);
        pedidoRepository.saveAndFlush(persistenceEntity);*/
    }

    @Override
    public long contar() {
        return pedidoRepository.count();
    }
    
    private void inserir(Pedido aggregateRoot) {
        PedidoPersistenceEntity persistenceEntity = assembler.fromDomainEntity(aggregateRoot);
        pedidoRepository.saveAndFlush(persistenceEntity);
        //aggregateRoot.setVersaoPedido(persistenceEntity.getVersaoPedido());
        atualizarVersaoPedido(aggregateRoot, persistenceEntity);
    }

    private void atualizar(Pedido aggregateRoot, PedidoPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.mergeEntity(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity); // não gerenciar de forma automática
        // atribui estado novamente (persistenceEntity)
        persistenceEntity = pedidoRepository.saveAndFlush(persistenceEntity);
        // atualiza versão do pedido (isso com setter publico)
        //aggregateRoot.setVersaoPedido(persistenceEntity.getVersaoPedido());
        atualizarVersaoPedido(aggregateRoot, persistenceEntity);
    }

    @SneakyThrows
    private void atualizarVersaoPedido(Pedido aggregateRoot, PedidoPersistenceEntity persistenceEntity) {
        Field versaoPedido = aggregateRoot.getClass().getDeclaredField("versaoPedido");
        versaoPedido.setAccessible(true);
        ReflectionUtils.setField(versaoPedido, aggregateRoot, persistenceEntity.getVersaoPedido());
        versaoPedido.setAccessible(false);
    }
}
