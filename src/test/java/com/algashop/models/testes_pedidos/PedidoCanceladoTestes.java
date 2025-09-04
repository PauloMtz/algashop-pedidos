package com.algashop.models.testes_pedidos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class PedidoCanceladoTestes {
    
    @Test
    void cancelarPedidoRetornaTrue() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        Assertions.assertThat(pedido.estaCancelado()).isFalse();
        pedido.cancelarPedido();
        Assertions.assertThat(pedido.estaCancelado()).isTrue();
    }

    @Test
    void cancelarPedidoRetornaFalse() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        pedido.adicionarItem(ProdutoTestesDataBuilder.novoProduto().build(), new Quantidade(2));

        Assertions.assertThat(pedido.estaCancelado()).isFalse();
    }

    @Test
    void cancelarPedidoEmOutroStatusRetornaFalse() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.PAGO).build();
        Assertions.assertThat(pedido.estaCancelado()).isFalse();
    }
}
