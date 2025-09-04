package com.algashop.models.testes_pedidos;

import java.time.LocalDate;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.exceptions.DataEntregaInvalidaException;
import com.algashop.domain.exceptions.ProdutoNaoTemEstoqueException;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.models.PedidoItem;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.ProdutoId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class PedidoTestes {
    
    @Test
    public void gerarNovoPedido() {
        ClienteId clienteId = new ClienteId();
        Pedido pedido = Pedido.rascunhoPedido(clienteId);

        Assertions.assertWith(pedido, 
            p -> Assertions.assertThat(p.getPedidoId()).isNotNull(),
            p -> Assertions.assertThat(p.getClienteId()).isEqualTo(clienteId),
            p -> Assertions.assertThat(p.getValorTotal()).isEqualTo(Moeda.ZERO),
            p -> Assertions.assertThat(p.getQtdeTotal()).isEqualTo(Quantidade.ZERO),
            p -> Assertions.assertThat(p.estaRascunho()).isTrue(),
            p -> Assertions.assertThat(p.getItens()).isEmpty(),

            p -> Assertions.assertThat(p.getFeitoEm()).isNull(),
            p -> Assertions.assertThat(p.getPagoEm()).isNull(),
            p -> Assertions.assertThat(p.getCanceladoEm()).isNull(),
            p -> Assertions.assertThat(p.getFinalizadoEm()).isNull(),
            p -> Assertions.assertThat(p.getFaturamento()).isNull(),
            p -> Assertions.assertThat(p.getEntrega()).isNull(),
            p -> Assertions.assertThat(p.getFormasPagamento()).isNull()
        );
    }

    @Test
    public void adicionarItemPedido() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        Produto produto = ProdutoTestesDataBuilder.produtoAleatorio().build();
        ProdutoId produtoId = produto.id();

        pedido.adicionarItem(produto, new Quantidade(1));

        Assertions.assertThat(pedido.getItens().size()).isEqualTo(1);
        PedidoItem pedidoItem = pedido.getItens().iterator().next();
        Assertions.assertWith(pedidoItem, 
            (item) -> Assertions.assertThat(item.getPedidoId()).isNotNull(),
            (item) -> Assertions.assertThat(item.getProdutoNome()).isEqualTo(new ProdutoNome("Produto Teste II")),
            (item) -> Assertions.assertThat(item.getProdutoId()).isEqualTo(produtoId),
            (item) -> Assertions.assertThat(item.getPreco()).isEqualTo(new Moeda("25")),
            (item) -> Assertions.assertThat(item.getQtde()).isEqualTo(new Quantidade(1))
        );
    }

    @Test
    public void gerarErroModificarListaItens() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        Produto produto = ProdutoTestesDataBuilder.produtoAleatorio().build();

        pedido.adicionarItem(produto, new Quantidade(1));

        Set<PedidoItem> itens = pedido.getItens();
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> itens.clear());
    }

    @Test
    public void recalcularTotal() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());

        pedido.adicionarItem(ProdutoTestesDataBuilder.produtoAleatorio().build(), 
            new Quantidade(2));

        pedido.adicionarItem(ProdutoTestesDataBuilder.produtoAleatorio().build(), 
            new Quantidade(3));

        // resultados esperados
        Assertions.assertThat(pedido.getValorTotal()).isEqualTo(new Moeda("125"));
        Assertions.assertThat(pedido.getQtdeTotal()).isEqualTo(new Quantidade(5));
    }

    @Test
    public void alterarFormaPagamento() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        pedido.alterarFormaPagamento(FormasPagamento.CREDITO);
        Assertions.assertWith(pedido.getFormasPagamento()).isEqualTo(FormasPagamento.CREDITO);
    }

    @Test
    public void alterarInformacoesFaturamento() {
        InformacoesCobranca faturamento = PedidoTestesDataBuilder.infoCobranca();

        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        pedido.alterarInfoFaturamento(faturamento);

        Assertions.assertThat(pedido.getFaturamento()).isEqualTo(faturamento);
    }

    @Test
    public void alterarInformacoesEntrega() {

        InformacoesEntrega entrega = PedidoTestesDataBuilder.infoEntrega();

        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        pedido.alterarEntrega(entrega);

        Assertions.assertWith(pedido, 
            p -> Assertions.assertThat(p.getEntrega()).isEqualTo(entrega)
        );
    }

    @Test
    public void entregaNaoPodeSerPassado() {

        LocalDate previsaoEntrega = LocalDate.now().minusDays(1);
        InformacoesEntrega entrega = PedidoTestesDataBuilder.infoEntrega().toBuilder()
            .previsaoEntrega(previsaoEntrega).build();

        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());

        Assertions.assertThatExceptionOfType(DataEntregaInvalidaException.class)
            .isThrownBy(() -> pedido.alterarEntrega(entrega));
    }

    @Test
    public void alterarQtdeItensPedido() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());

        pedido.adicionarItem(ProdutoTestesDataBuilder.produtoAleatorio().build(),
            new Quantidade(3));

        PedidoItem item = pedido.getItens().iterator().next();

        pedido.alterarQtdeItens(item.getPedidoItemId(), new Quantidade(5));

        Assertions.assertWith(pedido, 
            (p) -> Assertions.assertThat(p.getValorTotal()).isEqualTo(new Moeda("125")),
            (p) -> Assertions.assertThat(p.getQtdeTotal()).isEqualTo(new Quantidade(5))
        );
    }

    @Test
    public void verificarSeTemEstoque() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());

        ThrowableAssert.ThrowingCallable adicionarItem = () -> pedido.adicionarItem(ProdutoTestesDataBuilder.produtoIndisponível().build(),
            new Quantidade(1));
            
        Assertions.assertThatExceptionOfType(ProdutoNaoTemEstoqueException.class)
            .isThrownBy(adicionarItem);
    }
}
