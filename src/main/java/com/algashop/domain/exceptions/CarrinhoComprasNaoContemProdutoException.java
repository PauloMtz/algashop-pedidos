package com.algashop.domain.exceptions;

import com.algashop.domain.valueObjects.id.CarrinhoComprasId;
import com.algashop.domain.valueObjects.id.ProdutoId;

public class CarrinhoComprasNaoContemProdutoException extends DomainException {
    
    public CarrinhoComprasNaoContemProdutoException(CarrinhoComprasId id, ProdutoId produtoId) {
        super(String.format(MensagensErros.ERRO_CARRINHO_NAO_CONTEM_PRODUTO, id, produtoId));
    }
}
