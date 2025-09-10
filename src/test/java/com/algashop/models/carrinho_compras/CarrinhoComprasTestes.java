package com.algashop.models.carrinho_compras;

import java.math.BigDecimal;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.exceptions.CarrinhoComprasNaoContemItemException;
import com.algashop.domain.models.CarrinhoCompras;
import com.algashop.domain.models.CarrinhoComprasItem;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.CarrinhoComprasItemId;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class CarrinhoComprasTestes {
    
    @Test
    void iniciaCompra_inicializaCarrinhoVzio() {
        var clienteId = new ClienteId();

        CarrinhoCompras carrinho = CarrinhoCompras.iniciarCompra(clienteId);

        Assertions.assertWith(carrinho,
            c -> Assertions.assertThat(c.getId()).isNotNull(),
            c -> Assertions.assertThat(c.getClienteId()).isEqualTo(clienteId),
            c -> Assertions.assertThat(c.getValorTotal()).isEqualTo(Moeda.ZERO),
            c -> Assertions.assertThat(c.getQtdeItens()).isEqualTo(Quantidade.ZERO),
            c -> Assertions.assertThat(c.carrinhoEstaVazio()).isTrue(),
            c -> Assertions.assertThat(c.getItens()).isEmpty()
        );
    }

    @Test
    void adicionaItem_recalculaTotal() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras().metodoTemItens(false).gerarCarrinhoComprasBuild();
        Produto produto = ProdutoTestesDataBuilder.novoProduto().build();

        carrinho.adicionarItemCarrinho(produto, new Quantidade(2));

        Assertions.assertThat(carrinho.getItens()).hasSize(1);
        var item = carrinho.getItens().iterator().next();
        Assertions.assertThat(item.getProdutoId()).isEqualTo(produto.id());
        Assertions.assertThat(item.getQtde()).isEqualTo(new Quantidade(2));
        Assertions.assertThat(carrinho.getQtdeItens()).isEqualTo(new Quantidade(2));
        Assertions.assertThat(carrinho.getValorTotal()).isEqualTo(
            new Moeda(produto.preco().valor().multiply(new BigDecimal(2))));
    }

    @Test
    void incrementaQuantidade_mesmoProduto() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras().metodoTemItens(false).gerarCarrinhoComprasBuild();
        Produto produto = ProdutoTestesDataBuilder.novoProduto().build();

        carrinho.adicionarItemCarrinho(produto, new Quantidade(3));
        carrinho.adicionarItemCarrinho(produto, new Quantidade(2));
        var existeItem = carrinho.getItens().iterator().next();

        Set<CarrinhoComprasItem> itens = carrinho.getItens();
        Assertions.assertThat(itens).hasSize(1);
        Assertions.assertThat(existeItem.getQtde()).isEqualTo(new Quantidade(5));
    }

    @Test
    void removeItem_recalculaTotal() {
        // aqui não precisa do metodoTemItens, porém se tiver, tem que ser true
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras().metodoTemItens(true).gerarCarrinhoComprasBuild();
        var item = carrinho.getItens().iterator().next();

        carrinho.removerItem(item.getCarrinhoComprasItemId());

        Assertions.assertThat(carrinho.getItens()).doesNotContain(item);
        Assertions.assertThat(carrinho.getQtdeItens()).isEqualTo(
            new Quantidade(carrinho.getItens().stream()
                .mapToInt(i -> i.getQtde().valor()).sum())
        );
    }

    @Test
    void removerItemInexistente_lancarExcecao() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder.novoCarrinhoCompras().gerarCarrinhoComprasBuild();
        CarrinhoComprasItemId randomId = new CarrinhoComprasItemId();

        Assertions.assertThatExceptionOfType(CarrinhoComprasNaoContemItemException.class)
            .isThrownBy(() -> carrinho.removerItem(randomId));
    }

    @Test
    void carrinhoVazio_limpaItens_resetaTotal() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .metodoTemItens(false)
            .gerarCarrinhoComprasBuild();

        carrinho.carrinhoEstaVazio();

        Assertions.assertWith(carrinho,
            c -> Assertions.assertThat(c.carrinhoEstaVazio()).isTrue(),
            c -> Assertions.assertThat(c.getQtdeItens()).isEqualTo(Quantidade.ZERO),
            c -> Assertions.assertThat(c.getValorTotal()).isEqualTo(Moeda.ZERO)
        );
    }

    @Test
    void alteraPrecoItem_recalculaTotal() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .metodoTemItens(false)
            .gerarCarrinhoComprasBuild();

        Produto produto = ProdutoTestesDataBuilder.novoProduto().build();

        carrinho.adicionarItemCarrinho(produto, new Quantidade(2));

        produto = ProdutoTestesDataBuilder.novoProduto()
            .preco(new Moeda("100")).build();
        carrinho.atualizarItem(produto);

        var item = carrinho.buscarItem(produto.id());

        Assertions.assertThat(item.getPreco()).isEqualTo(new Moeda("100"));
        Assertions.assertThat(carrinho.getValorTotal()).isEqualTo(new Moeda("200"));
    }

    @Test
    void itemIndisponível() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .gerarCarrinhoComprasBuild();

        Produto produto = ProdutoTestesDataBuilder.novoProduto().temEstoque(false).build();
        carrinho.atualizarItem(produto);

        Assertions.assertThat(carrinho.contemItemIndisponivel()).isTrue();
    }

    @Test
    void quantidadeZero_lancaExcecao() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .gerarCarrinhoComprasBuild();

        var item = carrinho.getItens().iterator().next();

        Assertions.assertThatIllegalArgumentException()
            .isThrownBy(() -> carrinho.alterarItemQuantidade(item.getCarrinhoComprasItemId(), Quantidade.ZERO));
    }

    @Test
    void alteraQuantidade_recalculaTotal() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .gerarCarrinhoComprasBuild();

        var item = carrinho.getItens().iterator().next();

        carrinho.alterarItemQuantidade(item.getCarrinhoComprasItemId(), new Quantidade(5));

        Assertions.assertThat(carrinho.getQtdeItens()).isEqualTo(
            new Quantidade(carrinho.getItens().stream()
                .mapToInt(i -> i.getQtde().valor()).sum())
        );
    }

    @Test
    void buscarItem() {
        CarrinhoCompras carrinho = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .gerarCarrinhoComprasBuild();

        var item = carrinho.getItens().iterator().next();

        var encontrado = carrinho.buscarItem(item.getCarrinhoComprasItemId());

        Assertions.assertThat(encontrado).isEqualTo(item);
    }

    @Test
    public void comparaItensDiferentes() {
        CarrinhoCompras carrinho1 = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .gerarCarrinhoComprasBuild();

        CarrinhoCompras carrinho2 = CarrinhoComprasTestesDataBuilder
            .novoCarrinhoCompras()
            .gerarCarrinhoComprasBuild();

        Assertions.assertThat(carrinho1).isNotEqualTo(carrinho2);
    }
}
