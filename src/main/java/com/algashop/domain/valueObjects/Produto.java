package com.algashop.domain.valueObjects;

import java.util.Objects;

import com.algashop.domain.exceptions.ProdutoNaoTemEstoqueException;
import com.algashop.domain.valueObjects.id.ProdutoId;

import lombok.Builder;

@Builder
public record Produto(ProdutoId id, ProdutoNome nome, Moeda preco, Boolean temEstoque) {
    
    public Produto {
        Objects.requireNonNull(id);
        Objects.requireNonNull(nome);
        Objects.requireNonNull(preco);
        Objects.requireNonNull(temEstoque);
    }

    public void verificarSeTemEstoque() {
        if (!temEstoque) {
            throw new ProdutoNaoTemEstoqueException(this.id());
        }
    }
}
