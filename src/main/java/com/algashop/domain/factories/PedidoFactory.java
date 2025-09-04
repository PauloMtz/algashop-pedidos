package com.algashop.domain.factories;

import java.util.Objects;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;

public class PedidoFactory {
    
    private PedidoFactory() {}

    public static Pedido pedidoPreenchido(ClienteId clienteId, InformacoesEntrega entrega,
        InformacoesCobranca faturamento, FormasPagamento pagamento, Produto produto,
        Quantidade qtde) {

        Objects.requireNonNull(clienteId);
        Objects.requireNonNull(entrega);
        Objects.requireNonNull(faturamento);
        Objects.requireNonNull(pagamento);
        Objects.requireNonNull(produto);
        Objects.requireNonNull(qtde);

        Pedido pedido = Pedido.rascunhoPedido(clienteId);
        pedido.alterarEntrega(entrega);
        pedido.alterarInfoFaturamento(faturamento);
        pedido.alterarFormaPagamento(pagamento);
        pedido.adicionarItem(produto, qtde);
        
        return pedido;
    }
}
