package com.algashop.models.testes_pedidos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.exceptions.StatusPedidoNaoPodeAlterarException;
import com.algashop.domain.models.Pedido;

public class PedidoProntoTestes {
    
    @Test
    void pedidoProntoAtualizaStatus() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.PAGO).build();

        pedido.pedidoPronto();

        Assertions.assertWith(pedido,
                (p) -> Assertions.assertThat(p.getStatusPedido()).isEqualTo(PedidoStatus.PRONTO),
                (p) -> Assertions.assertThat(p.getPagoEm()).isNotNull()
        );
    }

    @Test
    void pedidoRascunhoLancaExcecao() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.RASCUNHO).build();

        Assertions.assertThatExceptionOfType(StatusPedidoNaoPodeAlterarException.class)
                .isThrownBy(pedido::pedidoPronto);

        Assertions.assertWith(pedido,
                (p) -> Assertions.assertThat(p.getStatusPedido()).isEqualTo(PedidoStatus.RASCUNHO),
                (p) -> Assertions.assertThat(p.getPagoEm()).isNotNull()
        );
    }

    @Test
    void pedidoFeitoLancaExcecao() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();

        Assertions.assertThatExceptionOfType(StatusPedidoNaoPodeAlterarException.class)
                .isThrownBy(() -> pedido.pedidoPronto()); 
                //.isThrownBy(pedido::pedidoPronto); // equivale à linha acima

        Assertions.assertWith(pedido,
                (p) -> Assertions.assertThat(p.getStatusPedido()).isEqualTo(PedidoStatus.FEITO),
                (p) -> Assertions.assertThat(p.getPagoEm()).isNotNull()
        );
    }

    @Test
    void pedidoProntoLancaExcecao() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.PRONTO).build();

        Assertions.assertThatExceptionOfType(StatusPedidoNaoPodeAlterarException.class)
                .isThrownBy(() -> pedido.pedidoPronto());

        Assertions.assertWith(pedido,
                (p) -> Assertions.assertThat(p.getStatusPedido()).isEqualTo(PedidoStatus.PRONTO),
                (p) -> Assertions.assertThat(p.getPagoEm()).isNotNull()
        );
    }
}
