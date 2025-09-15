package com.algashop.persistence.provider;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.algashop.domain.models.Pedido;
import com.algashop.domain.repository.PedidoRepositorio;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.persistence.assembler.PedidoPersistenceAssembler;
import com.algashop.persistence.disassembler.PedidoPersistenceDisassembler;
import com.algashop.persistence.entity.PedidoPersistenceEntity;
import com.algashop.persistence.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoPersistenceProvider implements PedidoRepositorio {

    //private final PedidoPersistenceEntity persistenceEntity;
    private final PedidoRepository pedidoRepository;
    private final PedidoPersistenceAssembler assembler;
    private final PedidoPersistenceDisassembler disassembler;

    @Override
    public Optional<Pedido> buscaId(PedidoId id) {
        Optional<PedidoPersistenceEntity> entidade = pedidoRepository.findById(id.valor().toLong());;
        return entidade.map(disassembler::paraClasseDominio);
    }

    @Override
    public boolean existente(PedidoId id) {
        return false;
    }

    @Override
    public void adicionar(Pedido aggregateRoot) {
        /*var persistenceEntity = PedidoPersistenceEntity.builder()
            .id(aggregateRoot.getId().valor().toLong())
            .clienteId(aggregateRoot.getClienteId().valor())
            .build();*/

        PedidoPersistenceEntity persistenceEntity = assembler.fromDomainEntity(aggregateRoot);
        pedidoRepository.saveAndFlush(persistenceEntity);
    }

    @Override
    public int contar() {
        return 0;
    }
    
}
