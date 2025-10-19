package com.algashop.persistence;

import static org.junit.jupiter.api.Assertions.assertTrue;

//import java.math.BigDecimal;
//import java.time.OffsetDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.repository.PedidoRepositorio;
//import com.algashop.domain.utils.IDGenerator;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.models.testes_pedidos.PedidoTestesDataBuilder;
import com.algashop.persistence.assembler.PedidoPersistenceAssembler;
import com.algashop.persistence.config.SpringDataAuditoriaConfig;
import com.algashop.persistence.disassembler.PedidoPersistenceDisassembler;
import com.algashop.persistence.entity.PedidoPersistenceEntity;
import com.algashop.persistence.provider.PedidoPersistenceProvider;
import com.algashop.persistence.repository.PedidoRepository;

//@SpringBootTest
//@Transactional
@DataJpaTest
@Import({PedidoPersistenceProvider.class, PedidoPersistenceAssembler.class, 
    PedidoPersistenceDisassembler.class, SpringDataAuditoriaConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PedidoPersistenceIT {

    /*
     * a @SpringBootTest carrega mais coisas e é mais pesada
     * a @DataJpaTest é mais leve, carrega apenas o contexto de persistência
     * além disso, a @DataJpaTest já carrega a @Transactional
     * JPA foi alterada para Jakarta Persistence, porém o spring boot não alterou o nome JPA
     * a @AutoConfigureTestDatabase é para a @DataJpaTest não substituir o banco configurado na aplicação
     */

    private final PedidoRepository pedidoRepository;
    private final PedidoRepositorio pedidoRepositorio;
    
    @Autowired
    public PedidoPersistenceIT(PedidoRepository pedidoRepository, PedidoRepositorio pedidoRepositorio) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoRepositorio = pedidoRepositorio;
    }

    @Test
    public void teste() {
        assertTrue("Hello".startsWith("H"));
    }

    @Test
    public void persistirRegistro() {

        PedidoPersistenceEntity pedido = PedidoPersistenceTestesDataBuilder.pedidoExistente().build();

        pedidoRepository.saveAndFlush(pedido);
        Assertions.assertThat(pedidoRepository.existsById(pedido.getId())).isTrue();

        PedidoPersistenceEntity entidadeEncontrada = pedidoRepository.findById(pedido.getId()).orElseThrow();

        Assertions.assertThat(entidadeEncontrada.getItens()).isNotEmpty();
    }

    // o @Transactional (do springboot) é para que um teste não influencie outro
    // sem ele ao executar todos os testes, esse teste abaixo não passaria, 
    // já que inseriu um registro no teste acima
    @Test
    public void contarRegistros() {
        long contarPedidos = pedidoRepository.count();
        Assertions.assertThat(contarPedidos).isZero();
    }

    @Test
    public void testePersistir_buscar() {
        Pedido pedidoOriginal = PedidoTestesDataBuilder.novoPedido().build();
        PedidoId pedidoId = pedidoOriginal.getId();
        pedidoRepositorio.adicionar(pedidoOriginal);
        Optional<Pedido> pedidoEncontrado = pedidoRepositorio.buscaId(pedidoId);
        Assertions.assertThat(pedidoEncontrado).isPresent();
        Pedido pedidoSalvo = pedidoEncontrado.get();

        Assertions.assertThat(pedidoSalvo).satisfies(
            p -> Assertions.assertThat(p.getId()).isEqualTo(pedidoId),
            p -> Assertions.assertThat(p.getClienteId()).isEqualTo(pedidoOriginal.getClienteId()),
            p -> Assertions.assertThat(p.getValorTotal()).isEqualTo(pedidoOriginal.getValorTotal()),
            p -> Assertions.assertThat(p.getQtdeTotal()).isEqualTo(pedidoOriginal.getQtdeTotal()),
            p -> Assertions.assertThat(p.getFeitoEm()).isEqualTo(pedidoOriginal.getFeitoEm()),
            p -> Assertions.assertThat(p.getPagoEm()).isEqualTo(pedidoOriginal.getPagoEm()),
            p -> Assertions.assertThat(p.getCanceladoEm()).isEqualTo(pedidoOriginal.getCanceladoEm()),
            p -> Assertions.assertThat(p.getFinalizadoEm()).isEqualTo(pedidoOriginal.getFinalizadoEm()),
            p -> Assertions.assertThat(p.getStatusPedido()).isEqualTo(pedidoOriginal.getStatusPedido()),
            p -> Assertions.assertThat(p.getFormasPagamento()).isEqualTo(pedidoOriginal.getFormasPagamento())
        );
    }

    @Test
    public void testaValoresAuditoria() {
        PedidoPersistenceEntity persistenceEntity = PedidoPersistenceTestesDataBuilder.pedidoExistente().build();
        persistenceEntity = pedidoRepository.saveAndFlush(persistenceEntity);

        Assertions.assertThat(persistenceEntity.getCriadoPorUsuarioID()).isNotNull();
        Assertions.assertThat(persistenceEntity.getUltimaModificacaoEm()).isNotNull();
        Assertions.assertThat(persistenceEntity.getUltimaModificacaoPorUsuarioID()).isNotNull();
    }

    @Test
    public void atualizaPedidoExistente() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        pedidoRepositorio.adicionar(pedido);

        pedido = pedidoRepositorio.buscaId(pedido.getId()).orElseThrow();
        pedido.pedidoPago();

        pedidoRepositorio.adicionar(pedido);

        pedido = pedidoRepositorio.buscaId(pedido.getId()).orElseThrow();

        Assertions.assertThat(pedido.estaPago()).isTrue();
    }

    @Test
    public void testeVersaoPedido() {
        /*
         * esse teste é para evitar que duas operações simultâneas ocorram
         * quando pagar o pedido, não pode permitir seu cancelamento
         */

        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        pedidoRepositorio.adicionar(pedido);

        Pedido pedido1 = pedidoRepositorio.buscaId(pedido.getId()).orElseThrow();
        Pedido pedido2 = pedidoRepositorio.buscaId(pedido.getId()).orElseThrow();

        pedido1.pedidoPago();
        pedidoRepositorio.adicionar(pedido1);

        pedido2.cancelarPedido();

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
            .isThrownBy(() -> pedidoRepositorio.adicionar(pedido2));

        Pedido pedidoSalvo = pedidoRepositorio.buscaId(pedido.getId()).orElseThrow();

        Assertions.assertThat(pedidoSalvo.getCanceladoEm()).isNull();
        Assertions.assertThat(pedidoSalvo.getPagoEm()).isNotNull();
    }

    @Test
    public void deveContarPedidosExistentes() {
        Assertions.assertThat(pedidoRepositorio.contar()).isZero();

        Pedido pedido1 = PedidoTestesDataBuilder.novoPedido().build();
        Pedido pedido2 = PedidoTestesDataBuilder.novoPedido().build();

        pedidoRepositorio.adicionar(pedido1);
        pedidoRepositorio.adicionar(pedido2);

        Assertions.assertThat(pedidoRepositorio.contar()).isEqualTo(2L);
    }

    @Test
    public void retornaPedidoExistente() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().build();
        pedidoRepositorio.adicionar(pedido);

        Assertions.assertThat(pedidoRepositorio.existente(pedido.getId())).isTrue();
        Assertions.assertThat(pedidoRepositorio.existente(new PedidoId())).isFalse();
    }
}
