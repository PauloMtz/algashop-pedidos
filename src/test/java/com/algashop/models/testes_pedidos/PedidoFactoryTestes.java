package com.algashop.models.testes_pedidos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.factories.PedidoFactory;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class PedidoFactoryTestes {
    
    @Test
    public void gerarPedidoPreenchidoParaConfirmacao() {

        InformacoesEntrega entrega = PedidoTestesDataBuilder.infoEntrega();
        InformacoesCobranca faturamento = PedidoTestesDataBuilder.infoCobranca();

        Produto produto = ProdutoTestesDataBuilder.novoProduto().build();
        FormasPagamento pagamento = FormasPagamento.DEBITO;
        Quantidade qtde = new Quantidade(2);
        ClienteId clienteId = new ClienteId();

        Pedido pedido = PedidoFactory.pedidoPreenchido(clienteId, entrega, faturamento, pagamento, produto, qtde);

        Assertions.assertWith(pedido, 
            p -> Assertions.assertThat(p.getEntrega()).isEqualTo(entrega),
            p -> Assertions.assertThat(p.getFaturamento()).isEqualTo(faturamento),
            p -> Assertions.assertThat(p.getFormasPagamento()).isEqualTo(pagamento),
            p -> Assertions.assertThat(p.getItens()).isNotEmpty(),
            p -> Assertions.assertThat(p.getClienteId()).isNotNull(),
            p -> Assertions.assertThat(p.estaRascunho()).isTrue()
        );

        pedido.confirmarPedido();
        Assertions.assertThat(pedido.estaFeito()).isTrue();
    }
}
