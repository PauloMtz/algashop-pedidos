package com.algashop.models.carrinho_compras;

import com.algashop.domain.models.CarrinhoComprasItem;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.CarrinhoComprasId;
import com.algashop.domain.valueObjects.id.ProdutoId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class CarrinhoComprasItemTestesDataBuilder {
    
    private CarrinhoComprasId carrinhoComprasId = CarrinhoComprasTestesDataBuilder.DEFAULT_CARRINHO_COMPRAS_ID;
    private ProdutoId produtoId = ProdutoTestesDataBuilder.DEFAULT_PRODUTO_ID;
    private ProdutoNome produtoNome = new ProdutoNome("Produto Teste");
    private Moeda precoProduto = new Moeda("100");
    private Quantidade quantidade = new Quantidade(1);
    private boolean produtoDisponivel = true;

    public CarrinhoComprasItemTestesDataBuilder() {}

    public static CarrinhoComprasItemTestesDataBuilder novoItemCarrinhoCompras() {
        return new CarrinhoComprasItemTestesDataBuilder();
    }

    public CarrinhoComprasItem gerarItemCarrinhoComprasBuild() {
        return CarrinhoComprasItem.novoItemCarrinho()
            .carrinhoComprasId(carrinhoComprasId)
            .produtoId(produtoId)
            .produtoNome(produtoNome)
            .preco(precoProduto)
            .quantidade(quantidade)
            .disponivel(produtoDisponivel)
            .build();
    }

    // setters alterados para CarrinhoComprasItemTestesDataBuilder
    public CarrinhoComprasItemTestesDataBuilder setCarrinhoComprasId(CarrinhoComprasId carrinhoComprasId) {
        this.carrinhoComprasId = carrinhoComprasId;
        return this;
    }

    public CarrinhoComprasItemTestesDataBuilder setProdutoId(ProdutoId produtoId) {
        this.produtoId = produtoId;
        return this;
    }

    public CarrinhoComprasItemTestesDataBuilder setProdutoNome(ProdutoNome produtoNome) {
        this.produtoNome = produtoNome;
        return this;
    }

    public CarrinhoComprasItemTestesDataBuilder setPrecoProduto(Moeda precoProduto) {
        this.precoProduto = precoProduto;
        return this;
    }

    public CarrinhoComprasItemTestesDataBuilder setQuantidade(Quantidade quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public CarrinhoComprasItemTestesDataBuilder setProdutoDisponivel(boolean produtoDisponivel) {
        this.produtoDisponivel = produtoDisponivel;
        return this;
    }
}
