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
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Import;

import com.algashop.domain.models.Pedido;
import com.algashop.domain.repository.PedidoRepositorio;
//import com.algashop.domain.utils.IDGenerator;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.models.testes_pedidos.PedidoTestesDataBuilder;
import com.algashop.persistence.assembler.PedidoPersistenceAssembler;
import com.algashop.persistence.disassembler.PedidoPersistenceDisassembler;
import com.algashop.persistence.entity.PedidoPersistenceEntity;
import com.algashop.persistence.provider.PedidoPersistenceProvider;
import com.algashop.persistence.repository.PedidoRepository;

//@SpringBootTest
//@Transactional
@DataJpaTest
@Import({PedidoPersistenceProvider.class, PedidoPersistenceAssembler.class, 
    PedidoPersistenceDisassembler.class})
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
        //long pedidoId = IDGenerator.generateTSID().toLong();

        /*PedidoPersistenceEntity pedido = PedidoPersistenceEntity.builder()
            .id(pedidoId)
            .clienteId(IDGenerator.generateTimeBaseUuid())
            .qtdeTotalItens(2)
            .valorTotal(new BigDecimal("1000"))
            .formaPagamento("DEBITO")
            .criadoEm(OffsetDateTime.now())
            .build();*/

        PedidoPersistenceEntity pedido = PedidoPersistenceTestesDataBuilder.pedidoExistente().build();

        pedidoRepository.saveAndFlush(pedido);
        //Assertions.assertThat(pedidoRepository.existsById(pedidoId)).isTrue();
        Assertions.assertThat(pedidoRepository.existsById(pedido.getId())).isTrue();
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
}
