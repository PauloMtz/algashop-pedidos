package com.algashop.domain.exceptions;

import com.algashop.domain.valueObjects.id.CarrinhoComprasItemId;
import com.algashop.domain.valueObjects.id.ProdutoId;

public class CarrinhoComprasItemIncompativelException extends DomainException {
    
    public CarrinhoComprasItemIncompativelException(CarrinhoComprasItemId itemId, ProdutoId produtoId) {
        super(String.format(MensagensErros.ERRO_CARRINHO_ITEM_INCOMPATIVEL, itemId, produtoId));
    }
}
