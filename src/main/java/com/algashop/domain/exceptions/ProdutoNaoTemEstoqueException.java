package com.algashop.domain.exceptions;

import com.algashop.domain.valueObjects.id.ProdutoId;

public class ProdutoNaoTemEstoqueException extends DomainException {
    
    public ProdutoNaoTemEstoqueException(ProdutoId produtoId) {
        super(String.format(MensagensErros.ERRO_PRODUTO_NAO_TEM_ESTOQUE, produtoId));
    }
}
