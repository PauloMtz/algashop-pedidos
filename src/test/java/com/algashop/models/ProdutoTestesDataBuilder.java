package com.algashop.models;

import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.id.ProdutoId;

public class ProdutoTestesDataBuilder {

    public static final ProdutoId DEFAULT_PRODUTO_ID = new ProdutoId();

    private ProdutoTestesDataBuilder() {}
    
    public static Produto.ProdutoBuilder novoProduto() {
        return Produto.builder()
            //.id(new ProdutoId())
            .id(DEFAULT_PRODUTO_ID)
            .temEstoque(true)
            .nome(new ProdutoNome("Produto Teste"))
            .preco(new Moeda("20"));
    }

    public static Produto.ProdutoBuilder produtoIndisponível() {
        return Produto.builder()
            //.id(new ProdutoId())
            .id(DEFAULT_PRODUTO_ID)
            .temEstoque(false)
            .nome(new ProdutoNome("Outro Produto Teste"))
            .preco(new Moeda("30"));
    }

    public static Produto.ProdutoBuilder produtoAleatorio() {
        return Produto.builder()
            //.id(new ProdutoId())
            .id(DEFAULT_PRODUTO_ID)
            .temEstoque(true)
            .nome(new ProdutoNome("Produto Teste II"))
            .preco(new Moeda("25"));
    }

    public static Produto.ProdutoBuilder produtoTesteIII() {
        return Produto.builder()
            //.id(new ProdutoId())
            .id(DEFAULT_PRODUTO_ID)
            .temEstoque(true)
            .nome(new ProdutoNome("Produto Teste III"))
            .preco(new Moeda("45"));
    }
}
