package com.algashop.models;

import org.junit.jupiter.api.Test;

import com.algashop.domain.models.PedidoItem;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.domain.valueObjects.id.ProdutoId;

public class PedidoItemTestes {
    
    @Test
    public void gerarNovoPedidoItem() {
        PedidoItem.novoPedidoItemBuilder()
            .produtoId(new ProdutoId())
            .qtde(new Quantidade(1))
            .pedidoId(new PedidoId())
            .produtoNome(new ProdutoNome("Nome do produto Teste"))
            .preco(new Moeda("50"))
        .build();
    }
}
