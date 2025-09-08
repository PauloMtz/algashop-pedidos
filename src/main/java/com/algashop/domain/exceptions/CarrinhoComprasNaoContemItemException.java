package com.algashop.domain.exceptions;

import com.algashop.domain.valueObjects.id.CarrinhoComprasId;
import com.algashop.domain.valueObjects.id.CarrinhoComprasItemId;

public class CarrinhoComprasNaoContemItemException extends DomainException {
    
    public CarrinhoComprasNaoContemItemException(CarrinhoComprasId id, CarrinhoComprasItemId itemId) {
        super(String.format(MensagensErros.ERRO_CARRINHO_NAO_CONTEM_ITEM, id, itemId));
    }
}
