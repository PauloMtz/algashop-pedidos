package com.algashop.models.testes_pedidos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.exceptions.StatusPedidoNaoPodeAlterarException;
import com.algashop.domain.models.Pedido;

public class PedidoStatusTestes {
    
    @Test
    public void podeAlterar() {
        Assertions.assertThat(PedidoStatus.RASCUNHO.podeAlterarStatus(PedidoStatus.FEITO)).isTrue();
        Assertions.assertThat(PedidoStatus.RASCUNHO.podeAlterarStatus(PedidoStatus.CANCELADO)).isTrue();
        Assertions.assertThat(PedidoStatus.PAGO.podeAlterarStatus(PedidoStatus.RASCUNHO)).isFalse();
    }

    @Test
    public void naoPodeAlterar() {
        Assertions.assertThat(PedidoStatus.PAGO.naoPodeAlterarStatus(PedidoStatus.RASCUNHO)).isTrue();
    }

    @Test
    public void alterarParaPedidoFeito() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().build();
        pedido.confirmarPedido();
        Assertions.assertThat(pedido.estaFeito()).isTrue();
    }

    @Test
    public void alterarParaPago() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();
        pedido.pedidoPago();

        Assertions.assertThat(pedido.estaPago()).isTrue();
        Assertions.assertThat(pedido.getPagoEm()).isNotNull();
    }

    @Test
    public void naoPodeAlterarParaPedidoFeito() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();

        Assertions.assertThatExceptionOfType(StatusPedidoNaoPodeAlterarException.class)
            .isThrownBy(() -> pedido.confirmarPedido());
    }
}
