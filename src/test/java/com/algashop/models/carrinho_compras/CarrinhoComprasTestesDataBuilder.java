package com.algashop.models.carrinho_compras;

import com.algashop.domain.models.CarrinhoCompras;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.CarrinhoComprasId;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.models.ProdutoTestesDataBuilder;

public class CarrinhoComprasTestesDataBuilder {
    
    public static final CarrinhoComprasId DEFAULT_CARRINHO_COMPRAS_ID = new CarrinhoComprasId();
    public ClienteId clienteId = new ClienteId();
    private boolean temItens = true;

    public CarrinhoComprasTestesDataBuilder() {}

    public static CarrinhoComprasTestesDataBuilder novoCarrinhoCompras() {
        return new CarrinhoComprasTestesDataBuilder();
    }

    public CarrinhoCompras gerarCarrinhoComprasBuild() {
        CarrinhoCompras carrinhoCompras = CarrinhoCompras.iniciarCompra(clienteId);

        if (temItens) {
            carrinhoCompras.adicionarItemCarrinho(ProdutoTestesDataBuilder.novoProduto().build(),
                new Quantidade(2));

            carrinhoCompras.adicionarItemCarrinho(ProdutoTestesDataBuilder.novoProduto().build(),
                new Quantidade(3));
        }

        return carrinhoCompras;
    }

    public CarrinhoComprasTestesDataBuilder clienteId(ClienteId idCliente) {
        this.clienteId = idCliente;
        return this;
    }

    public CarrinhoComprasTestesDataBuilder metodoTemItens(boolean temItens) {
        this.temItens = temItens;
        return this;
    }
}
