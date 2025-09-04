package com.algashop.models.testes_pedidos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.exceptions.PedidoNaoContemItemException;
import com.algashop.domain.exceptions.PedidoNaoPodeEditarException;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.models.PedidoItem;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.PedidoItemId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class PedidoRemoverItemTestes {
    
    @Test
    void removerItemRecalculcarValorTotal() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());

        pedido.adicionarItem(
                ProdutoTestesDataBuilder.novoProduto().build(),
                new Quantidade(2)
        );

        PedidoItem item1 = pedido.getItens().iterator().next();

        pedido.adicionarItem(
                ProdutoTestesDataBuilder.produtoAleatorio().build(),
                new Quantidade(3)
        );

        pedido.removerItemPedido(item1.getPedidoItemId());

        Assertions.assertWith(pedido,
                (i) -> Assertions.assertThat(i.getValorTotal()).isEqualTo(new Moeda("75.00")),
                (i) -> Assertions.assertThat(i.getQtdeTotal()).isEqualTo(new Quantidade(3))
        );
    }

    @Test
    void removerItemNaoExistenteLancaExcecao() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().build();

        Assertions.assertThatExceptionOfType(PedidoNaoContemItemException.class)
                .isThrownBy(()-> pedido.removerItemPedido(new PedidoItemId()));

        Assertions.assertWith(pedido,
                (i) -> Assertions.assertThat(i.getValorTotal()).isEqualTo(new Moeda("120.00")),
                (i) -> Assertions.assertThat(i.getQtdeTotal()).isEqualTo(new Quantidade(5))
        );
    }

    @Test
    void removerItemLancaExcecao() {
        Pedido pedido = PedidoTestesDataBuilder.novoPedido().setStatusPedido(PedidoStatus.FEITO).build();

        Assertions.assertThatExceptionOfType(PedidoNaoPodeEditarException.class)
                .isThrownBy(()->pedido.removerItemPedido(new PedidoItemId()));

        Assertions.assertWith(pedido,
                (i) -> Assertions.assertThat(i.getValorTotal()).isEqualTo(new Moeda("120.00")),
                (i) -> Assertions.assertThat(i.getQtdeTotal()).isEqualTo(new Quantidade(5))
        );
    }
}
