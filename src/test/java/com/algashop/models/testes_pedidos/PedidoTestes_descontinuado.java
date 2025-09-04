package com.algashop.models.testes_pedidos;

import java.time.LocalDate;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.exceptions.DataEntregaInvalidaException;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.models.PedidoItem;
import com.algashop.domain.valueObjects.ClienteCEP;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClienteTelefone;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.ProdutoId;

public class PedidoTestes_descontinuado {
    
    @Test
    public void gerarNovoPedido() {
        Pedido.rascunhoPedido(new ClienteId());
    }

    /*@Test
    public void adicionarItemPedido() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());

        pedido.adicionarItem(
            new ProdutoId(),
            new ProdutoNome("Nome do Produto Teste"),
            new Moeda("50"),
            new Quantidade(1)
        );

        Assertions.assertThat(pedido.getItens()).isNotEmpty();
    }*/

    @Test
    public void adicionarItemPedido() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        ProdutoId produtoId = new ProdutoId();

        pedido.adicionarItem(
            produtoId,
            new ProdutoNome("Nome do Produto Teste"),
            new Moeda("50"),
            new Quantidade(1)
        );

        Assertions.assertThat(pedido.getItens().size()).isEqualTo(1);
        PedidoItem pedidoItem = pedido.getItens().iterator().next();
        Assertions.assertWith(pedidoItem, 
            (item) -> Assertions.assertThat(item.getPedidoId()).isNotNull(),
            (item) -> Assertions.assertThat(item.getProdutoNome()).isEqualTo(new ProdutoNome("Nome do Produto Teste")),
            (item) -> Assertions.assertThat(item.getProdutoId()).isEqualTo(produtoId),
            (item) -> Assertions.assertThat(item.getPreco()).isEqualTo(new Moeda("50")),
            (item) -> Assertions.assertThat(item.getQtde()).isEqualTo(new Quantidade(1))
        );
    }

    @Test
    public void gerarErroModificarListaItens() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        ProdutoId produtoId = new ProdutoId();

        pedido.adicionarItem(
            produtoId,
            new ProdutoNome("Nome do Produto Teste"),
            new Moeda("50"),
            new Quantidade(1)
        );

        Set<PedidoItem> itens = pedido.getItens();
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> itens.clear());
    }

    @Test
    public void recalcularTotal() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        ProdutoId produtoId = new ProdutoId();

        pedido.adicionarItem(
            produtoId,
            new ProdutoNome("Nome do Produto Teste"),
            new Moeda("50"),
            new Quantidade(2)
        );

        pedido.adicionarItem(
            produtoId,
            new ProdutoNome("Outro Produto Teste"),
            new Moeda("70"),
            new Quantidade(3)
        );

        // resultados esperados
        Assertions.assertThat(pedido.getValorTotal()).isEqualTo(new Moeda("310"));
        Assertions.assertThat(pedido.getQtdeTotal()).isEqualTo(new Quantidade(5));
    }

    @Test
    public void alterarFormaPagamento() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        pedido.alterarFormaPagamento(FormasPagamento.CREDITO);
        Assertions.assertWith(pedido.getFormasPagamento()).isEqualTo(FormasPagamento.CREDITO);
    }

    @Test
    public void alterarInformacoesFaturamento() {
        ClienteEndereco endereco = ClienteEndereco.builder()
            .logradouro("Rua Teste, 11")
            .complemento("teste")
            .bairro("Bairro Teste")
            .cidade("Cidade Teste")
            .estado("TT")
            .cep(new ClienteCEP("12345678"))
        .build();

        InformacoesCobranca faturamento = InformacoesCobranca.builder()
            .endereco(endereco)
            .cpf(new ClienteCPF("123.456.789-01"))
            .telefone(new ClienteTelefone("(00) 0001-0002"))
            .nome(new ClienteNome("Fulano", "de Tal"))
        .build();

        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        pedido.alterarInfoFaturamento(faturamento);

        Assertions.assertThat(pedido.getFaturamento()).isEqualTo(faturamento);
    }

    @Test
    public void alterarInformacoesEntrega() {
        ClienteEndereco endereco = ClienteEndereco.builder()
            .logradouro("Rua Teste, 11")
            .complemento("teste")
            .bairro("Bairro Teste")
            .cidade("Cidade Teste")
            .estado("TT")
            .cep(new ClienteCEP("12345678"))
        .build();

        InformacoesEntrega entrega = InformacoesEntrega.builder()
            .endereco(endereco)
            .nome(new ClienteNome("Fulano", "de Tal"))
            .cpf(new ClienteCPF("123.456.789-01"))
            .telefone(new ClienteTelefone("(01) 0001-0002"))
        .build();

        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        Moeda valorEntrega = Moeda.ZERO;
        LocalDate previsaoEntrega = LocalDate.now().plusDays(1);
        pedido.alterarEntrega(entrega, valorEntrega, previsaoEntrega);

        Assertions.assertWith(pedido, 
            p -> Assertions.assertThat(p.getEntrega()).isEqualTo(entrega),
            p -> Assertions.assertThat(p.getValorEntrega()).isEqualTo(valorEntrega),
            p -> Assertions.assertThat(p.getPrevisaoEntrega()).isEqualTo(previsaoEntrega)
        );
    }

    @Test
    public void entregaNaoPodeSerPassado() {
        ClienteEndereco endereco = ClienteEndereco.builder()
            .logradouro("Rua Teste, 11")
            .complemento("teste")
            .bairro("Bairro Teste")
            .cidade("Cidade Teste")
            .estado("TT")
            .cep(new ClienteCEP("12345678"))
        .build();

        InformacoesEntrega entrega = InformacoesEntrega.builder()
            .endereco(endereco)
            .nome(new ClienteNome("Fulano", "de Tal"))
            .cpf(new ClienteCPF("123.456.789-01"))
            .telefone(new ClienteTelefone("(01) 0001-0002"))
        .build();

        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());
        Moeda valorEntrega = Moeda.ZERO;
        LocalDate previsaoEntrega = LocalDate.now().minusDays(1);

        Assertions.assertThatExceptionOfType(DataEntregaInvalidaException.class)
            .isThrownBy(() -> pedido.alterarEntrega(entrega, valorEntrega, previsaoEntrega));
    }

    @Test
    public void alterarQtdeItensPedido() {
        Pedido pedido = Pedido.rascunhoPedido(new ClienteId());

        pedido.adicionarItem(new ProdutoId(), new ProdutoNome("Produto Teste"), 
            new Moeda("20"), new Quantidade(3));

        // vai iterar pelo pedido que só tem 01 item
        PedidoItem item = pedido.getItens().iterator().next();

        pedido.alterarQtdeItens(item.getPedidoItemId(), new Quantidade(5));

        Assertions.assertWith(pedido, 
            (p) -> Assertions.assertThat(p.getValorTotal()).isEqualTo(new Moeda("100")),
            (p) -> Assertions.assertThat(p.getQtdeTotal()).isEqualTo(new Quantidade(5))
        );
    }
}
