package com.algashop.domain.exceptions;

public class MensagensErros {
    
    public static final String VALIDACAO_ERRO_EMAIL_INVALIDO = "O e-mail deve ser válido";
    public static final String VALIDACAO_ERRO_NASCIMENTO = "A data de nascimento deve ser válida";
    public static final String VALIDACAO_ERRO_NOME_NULO = "O nome não pode ser nulo";
    public static final String VALIDACAO_ERRO_NOME_BRANCO = "O campo nome deve ser preenchido";
    public static final String ERRO_CLIENTE_ARQUIVADO = "O cliente já está arquivado";
    public static final String ERRO_ALTERAR_STATUS_PEDIDO = "O pedido de id %s não pode ser alterado de %s para %s";
    public static final String ERRO_DATA_ENTREGA_INVALIDA = "A data de entrega do pedido %s não pode ser no passado";

    // mensagens de erro para PedidoNaoFeitoException
    public static final String ERRO_PEDIDO_NAO_FEITO = "Adicione itens ao pedido %s";
    public static final String ERRO_PEDIDO_INFO_ENTREGA = "Pedido %s está com informações de entrega inválidas";
    public static final String ERRO_PEDIDO_INFO_COBRANCA = "Pedido %s está com informações de faturamento inválidas";
    public static final String ERRO_PEDIDO_VALOR_ENTREGA = "Pedido %s está com valor de entrega inválido";
    public static final String ERRO_PEDIDO_PREVISAO_ENTREGA = "Pedido %s está com previsão de entrega inválida";
    public static final String ERRO_PEDIDO_METODO_PAGAMENTO = "Pedido %s está com método de pagamento inválido";

    public static final String ERRO_PEDIDO_NAO_TEM_ITEM = "Adicione itens ao pedido %s";
}
