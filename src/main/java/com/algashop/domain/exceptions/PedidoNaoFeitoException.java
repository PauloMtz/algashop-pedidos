package com.algashop.domain.exceptions;

import com.algashop.domain.valueObjects.id.PedidoId;

public class PedidoNaoFeitoException extends DomainException {
    
    /*public PedidoNaoFeitoException(PedidoId id) {
        super(String.format(MensagensErros.ERRO_PEDIDO_NAO_FEITO, id));
    }*/

    private PedidoNaoFeitoException(String mensagem) {
        super(mensagem);
    }

    public static PedidoNaoFeitoException semItens(PedidoId pedidoId) {
        return new PedidoNaoFeitoException(
            String.format(MensagensErros.ERRO_PEDIDO_NAO_FEITO, pedidoId));
    }

    public static PedidoNaoFeitoException semInformacoesEntrega(PedidoId pedidoId) {
        return new PedidoNaoFeitoException(
            String.format(MensagensErros.ERRO_PEDIDO_INFO_ENTREGA, pedidoId));
    }

    public static PedidoNaoFeitoException semInformacoesCobranca(PedidoId pedidoId) {
        return new PedidoNaoFeitoException(
            String.format(MensagensErros.ERRO_PEDIDO_INFO_COBRANCA, pedidoId));
    }

    public static PedidoNaoFeitoException valorEntregaInvalido(PedidoId pedidoId) {
        return new PedidoNaoFeitoException(
            String.format(MensagensErros.ERRO_PEDIDO_VALOR_ENTREGA, pedidoId));
    }

    public static PedidoNaoFeitoException previsaoEntregaInvalida(PedidoId pedidoId) {
        return new PedidoNaoFeitoException(
            String.format(MensagensErros.ERRO_PEDIDO_PREVISAO_ENTREGA, pedidoId));
    }

    public static PedidoNaoFeitoException metodoPagamentoInvalido(PedidoId pedidoId) {
        return new PedidoNaoFeitoException(
            String.format(MensagensErros.ERRO_PEDIDO_METODO_PAGAMENTO, pedidoId));
    }
}
