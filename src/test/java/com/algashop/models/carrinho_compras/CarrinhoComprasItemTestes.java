package com.algashop.models.carrinho_compras;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.models.CarrinhoComprasItem;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.CarrinhoComprasId;
import com.algashop.domain.valueObjects.id.CarrinhoComprasItemId;
import com.algashop.domain.valueObjects.id.ProdutoId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class CarrinhoComprasItemTestes {
    
    @Test
    public void inicializaCarrinhoItem() {
        CarrinhoComprasItem item = CarrinhoComprasItemTestesDataBuilder.novoItemCarrinhoCompras()
            .setProdutoNome(new ProdutoNome("Notebook"))
            .setPrecoProduto(new Moeda("2000"))
            .setQuantidade(new Quantidade(2))
            .setProdutoDisponivel(true)
            .gerarItemCarrinhoComprasBuild();

        Assertions.assertWith(item,
            i -> Assertions.assertThat(i.getCarrinhoComprasItemId()).isNotNull(),
            i -> Assertions.assertThat(i.getCarrinhoComprasId()).isNotNull(),
            i -> Assertions.assertThat(i.getProdutoId()).isNotNull(),
            i -> Assertions.assertThat(i.getNome()).isEqualTo(new ProdutoNome("Notebook")),
            i -> Assertions.assertThat(i.getPreco()).isEqualTo(new Moeda("2000")),
            i -> Assertions.assertThat(i.getQtde()).isEqualTo(new Quantidade(2)),
            i -> Assertions.assertThat(i.getDisponivel()).isTrue(),
            i -> Assertions.assertThat(i.getValorTotal()).isEqualTo(new Moeda("4000"))
        );
    }

    @Test
    public void alterarQuantidade_recalculaTotal() {
        CarrinhoComprasItem item = CarrinhoComprasItemTestesDataBuilder.novoItemCarrinhoCompras()
            .setPrecoProduto(new Moeda("1000"))
            .setQuantidade(new Quantidade(1))
            .gerarItemCarrinhoComprasBuild();

        item.alterarQuantidade(new Quantidade(3));

        Assertions.assertWith(item,
            i -> Assertions.assertThat(i.getQtde()).isEqualTo(new Quantidade(3)),
            i -> Assertions.assertThat(i.getValorTotal()).isEqualTo(new Moeda("3000"))
        );
    }

    @Test
    public void alterarPreco_recalculaTotal() {
        CarrinhoComprasItem item = CarrinhoComprasItemTestesDataBuilder.novoItemCarrinhoCompras()
            .setPrecoProduto(new Moeda("1500"))
            .setQuantidade(new Quantidade(2))
            .gerarItemCarrinhoComprasBuild();

        Produto produto = ProdutoTestesDataBuilder.novoProduto().build();
        item.atualizar(produto);

        Assertions.assertWith(item,
            i -> Assertions.assertThat(i.getPreco()).isEqualTo(produto.preco()),
            i -> Assertions.assertThat(i.getValorTotal()).isEqualTo(produto.preco().multiplicar(new Quantidade(2)))
        );
    }

    @Test
    public void alterarDisponibilidade_atualizaStatus() {
        CarrinhoComprasItem item = CarrinhoComprasItemTestesDataBuilder.novoItemCarrinhoCompras()
            .setProdutoDisponivel(true)
            .gerarItemCarrinhoComprasBuild();

        Produto produto = ProdutoTestesDataBuilder.novoProduto()
            .temEstoque(false)
            .build();

        item.atualizar(produto);

        Assertions.assertThat(item.getDisponivel()).isFalse();
    }

    @Test
    public void compararItensIguais() {
        CarrinhoComprasId carrinhoId = new CarrinhoComprasId();
        ProdutoId produtoId = new ProdutoId();
        CarrinhoComprasItemId itemId = new CarrinhoComprasItemId();

        CarrinhoComprasItem item1 = CarrinhoComprasItem.itemExistente()
            .carrinhoComprasItemId(itemId)
            .carrinhoComprasId(carrinhoId)
            .produtoId(produtoId)
            .nome(new ProdutoNome("Mouse"))
            .preco(new Moeda("100"))
            .qtde(new Quantidade(1))
            .disponivel(true)
            .valorTotal(new Moeda("100"))
            .build();

        CarrinhoComprasItem item2 = CarrinhoComprasItem.itemExistente()
            .carrinhoComprasItemId(itemId)
            .carrinhoComprasId(carrinhoId)
            .produtoId(produtoId)
            .nome(new ProdutoNome("Notebook"))
            .preco(new Moeda("100"))
            .qtde(new Quantidade(1))
            .disponivel(true)
            .valorTotal(new Moeda("100"))
            .build();

        Assertions.assertThat(item1).isEqualTo(item2);
        Assertions.assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    public void compararItensDiferentes() {
        CarrinhoComprasItem item1 = CarrinhoComprasItemTestesDataBuilder
            . novoItemCarrinhoCompras()
            .gerarItemCarrinhoComprasBuild();

        CarrinhoComprasItem item2 = CarrinhoComprasItemTestesDataBuilder
            . novoItemCarrinhoCompras()
            .gerarItemCarrinhoComprasBuild();

        Assertions.assertThat(item1).isNotEqualTo(item2);
    }
}
