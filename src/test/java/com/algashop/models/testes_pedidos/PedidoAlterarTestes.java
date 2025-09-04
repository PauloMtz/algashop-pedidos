package com.algashop.models.testes_pedidos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.exceptions.PedidoNaoPodeEditarException;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.models.PedidoItem;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.models.ProdutoTestesDataBuilder;

public class PedidoAlterarTestes {
    
    @Test
    void alteracaoRascunhoNaoException() {
        Pedido pedidoRascunho = PedidoTestesDataBuilder.novoPedido().build();
        Produto produto = ProdutoTestesDataBuilder.novoProduto().build();
        Quantidade quantidade = new Quantidade(2);
        InformacoesCobranca faturamento = PedidoTestesDataBuilder.infoCobranca();
        InformacoesEntrega entrega = PedidoTestesDataBuilder.infoEntrega();
        FormasPagamento formaPagamento = FormasPagamento.CREDITO;

        PedidoItem pedidoItem = pedidoRascunho.getItens().iterator().next();

        Assertions.assertThatCode(() -> pedidoRascunho.adicionarItem(produto, quantidade)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> pedidoRascunho.alterarInfoFaturamento(faturamento)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> pedidoRascunho.alterarEntrega(entrega)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> pedidoRascunho.alterarQtdeItens(pedidoItem.getPedidoItemId(), quantidade)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> pedidoRascunho.alterarFormaPagamento(formaPagamento)).doesNotThrowAnyException();
    }

    @Test
    void alteracaoFaturamentoException() {
        Pedido pedidoConfirmado = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        InformacoesCobranca faturamento = PedidoTestesDataBuilder.infoCobranca();

        Assertions.assertThatThrownBy(() -> pedidoConfirmado.alterarInfoFaturamento(faturamento))
                .isInstanceOf(PedidoNaoPodeEditarException.class);
    }

    @Test
    void alteracaoEntregaException() {
        Pedido pedidoConfirmado = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        InformacoesEntrega entrega = PedidoTestesDataBuilder.infoEntrega();

        Assertions.assertThatThrownBy(() -> pedidoConfirmado.alterarEntrega(entrega))
                .isInstanceOf(PedidoNaoPodeEditarException.class);
    }

    @Test
    void alteracaoQuantidadeException() {
        Pedido pedidoConfirmado = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        Quantidade qtde = new Quantidade(5);

        PedidoItem pedidoItem = pedidoConfirmado.getItens().iterator().next();

        Assertions.assertThatThrownBy(() -> pedidoConfirmado.alterarQtdeItens(pedidoItem.getPedidoItemId(), qtde))
                .isInstanceOf(PedidoNaoPodeEditarException.class);
    }

    @Test
    void alteracaoFormaPagamentoException() {
        Pedido pedidoConfirmado = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        FormasPagamento formaPagamento = FormasPagamento.DEBITO;

        Assertions.assertThatThrownBy(() -> pedidoConfirmado.alterarFormaPagamento(formaPagamento))
                .isInstanceOf(PedidoNaoPodeEditarException.class);
    }
}
