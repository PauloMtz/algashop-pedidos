package com.algashop.models.testes_pedidos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.models.PedidoItem;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class PedidoItemTestes {

    @Test
    public void gerarNovoPedidoItem() {
        Produto produto = ProdutoTestesDataBuilder.novoProduto().build();
        Quantidade qtde = new Quantidade(1);
        PedidoId pedidoId = new PedidoId();

        PedidoItem pedidoItem = PedidoItem.novoPedidoItemBuilder()
            .produto(produto)
            .qtde(new Quantidade(1))
            .pedidoId(pedidoId)
        .build();

        Assertions.assertWith(pedidoItem, 
            p -> Assertions.assertThat(p.getPedidoId()).isNotNull(),
            p -> Assertions.assertThat(p.getProdutoId()).isEqualTo(produto.id()),
            p -> Assertions.assertThat(p.getProdutoNome()).isEqualTo(produto.nome()),
            p -> Assertions.assertThat(p.getPreco()).isEqualTo(produto.preco()),
            p -> Assertions.assertThat(p.getQtde()).isEqualTo(qtde),
            p -> Assertions.assertThat(p.getPedidoId()).isEqualTo(pedidoId)
        );
    }
}
