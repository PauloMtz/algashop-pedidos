package com.algashop.models;

import java.time.LocalDate;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.utils.IDGenerator;
import com.algashop.domain.valueObjects.ClienteCEP;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEmail;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClienteTelefone;
import com.algashop.domain.valueObjects.Destinatario;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;

public class PedidoTestesDataBuilder {
    
    /*public static Pedido.PedidoExistenteBuilder pedidoExistente() {
        return Pedido.pedidoExistenteBuilder()
            .atributos aqui
        .build();
    }*/

    private ClienteId clienteId = new ClienteId(IDGenerator.generateTimeBaseUuid());
    private FormasPagamento formaPagamento = FormasPagamento.DEBITO;
    //private Moeda valorEntrega = new Moeda("10.00");
    //private LocalDate previsaoEntrega = LocalDate.now().plusWeeks(1);
    private InformacoesEntrega entrega = infoEntrega();
    private InformacoesCobranca faturamento = infoCobranca();
    private boolean pedidoTemItens = true;
    private PedidoStatus statusPedido = PedidoStatus.RASCUNHO;
    
    private PedidoTestesDataBuilder() {}

    public static PedidoTestesDataBuilder novoPedido() {
        return new PedidoTestesDataBuilder();
    }

    public Pedido build() {
        Pedido pedido = Pedido.rascunhoPedido(clienteId);
        pedido.alterarEntrega(entrega/*valorEntrega, previsaoEntrega*/);
        pedido.alterarInfoFaturamento(faturamento);
        pedido.alterarFormaPagamento(formaPagamento);

        if (pedidoTemItens) {
            pedido.adicionarItem(
                ProdutoTestesDataBuilder.novoProduto().build(), 
                new Quantidade(3)
            );

            pedido.adicionarItem(
                ProdutoTestesDataBuilder.produtoAleatorio().build(), 
                new Quantidade(2)
            );
        }

        switch (this.statusPedido) {
            case RASCUNHO -> {}
            case FEITO -> {
                pedido.confirmarPedido();
            }
            case PAGO -> {
                pedido.confirmarPedido();
                pedido.pedidoPago();
            }
            case PRONTO -> {}
            case CANCELADO -> {}
        }

        return pedido;
    }

    public static InformacoesEntrega infoEntrega() {
        return InformacoesEntrega.builder()
            .valorEntrega(new Moeda("10"))
            .previsaoEntrega(LocalDate.now().plusWeeks(1))
            .endereco(enderecoCliente())
            .cliente(Destinatario.builder()
                .nome(new ClienteNome("Fulano", "de Tal"))
                .cpf(new ClienteCPF("123.456.789-01"))
                .telefone(new ClienteTelefone("(01) 0001-0002"))
            .build())
        .build();
    }

    public static InformacoesEntrega infoEntregaAlternativa() {
        return InformacoesEntrega.builder()
            .valorEntrega(new Moeda("20.00"))
            .previsaoEntrega(LocalDate.now().plusWeeks(2))
            .endereco(enderecoCliente())
            .cliente(Destinatario.builder()
                .nome(new ClienteNome("Ciclano", "de Tal"))
                .cpf(new ClienteCPF("123.456.789-01"))
                .telefone(new ClienteTelefone("(02) 0003-0004"))
            .build())
        .build();
    }

    public static InformacoesCobranca infoCobranca() {
        return InformacoesCobranca.builder()
            .endereco(enderecoCliente())
            .cpf(new ClienteCPF("123.456.789-01"))
            .telefone(new ClienteTelefone("(00) 0001-0002"))
            .nome(new ClienteNome("Fulano", "de Tal"))
            .email(new ClienteEmail("fulano@email.com"))
        .build();
    }

    public static ClienteEndereco enderecoCliente() {
        return ClienteEndereco.builder()
            .logradouro("Rua Teste, 11")
            .complemento("teste")
            .bairro("Bairro Teste")
            .cidade("Cidade Teste")
            .estado("TT")
            .cep(new ClienteCEP("12345678"))
        .build();
    }

    public static ClienteEndereco enderecoAlternativo() {
        return ClienteEndereco.builder()
            .logradouro("Rua Teste Alternativo, 101")
            .complemento("teste alternativo")
            .bairro("Bairro Teste Alternativo")
            .cidade("Cidade Teste Alternativo")
            .estado("TT")
            .cep(new ClienteCEP("12345678"))
        .build();
    }

    // setters da classe builder
    // gera os setters, troca o void pelo nome da classe e adiciona o retorno
    public PedidoTestesDataBuilder setClienteId(ClienteId clienteId) {
        this.clienteId = clienteId;
        return this;
    }

    public PedidoTestesDataBuilder setFormaPagamento(FormasPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
        return this;
    }

    /*public PedidoTestesDataBuilder setValorEntrega(Moeda valorEntrega) {
        this.valorEntrega = valorEntrega;
        return this;
    }

    public PedidoTestesDataBuilder setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
        return this;
    }*/

    public PedidoTestesDataBuilder setEntrega(InformacoesEntrega entrega) {
        this.entrega = entrega;
        return this;
    }

    public PedidoTestesDataBuilder setFaturamento(InformacoesCobranca faturamento) {
        this.faturamento = faturamento;
        return this;
    }

    public PedidoTestesDataBuilder setPedidoTemItens(boolean pedidoTemItens) {
        this.pedidoTemItens = pedidoTemItens;
        return this;
    }

    public PedidoTestesDataBuilder setStatusPedido(PedidoStatus statusPedido) {
        this.statusPedido = statusPedido;
        return this;
    }
}
