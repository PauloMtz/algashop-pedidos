package com.algashop.domain.models;

import java.util.Objects;

import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.domain.valueObjects.id.PedidoItemId;
import com.algashop.domain.valueObjects.id.ProdutoId;

import lombok.Builder;

public class PedidoItem {

    private PedidoItemId pedidoItemId;
    private PedidoId pedidoId;
    private ProdutoId produtoId;
    private ProdutoNome produtoNome;
    private Moeda preco;
    private Quantidade qtde;
    private Moeda valorTotal;

    // construtor com todos os parâmetros
    @Builder(builderClassName = "PedidoItemExistente", builderMethodName = "pedidoItemExistente")
    public PedidoItem(PedidoItemId pedidoItemId, PedidoId pedidoId, ProdutoId produtoId, 
        ProdutoNome produtoNome, Moeda preco, Quantidade qtde, Moeda valorTotal) {

        this.setPedidoItemId(pedidoItemId);
        this.setPedidoId(pedidoId);
        this.setProdutoId(produtoId);
        this.setProdutoNome(produtoNome);
        this.setPreco(preco);
        this.setQtde(qtde);
        this.setValorTotal(valorTotal);
    }

    // factory
    @Builder(builderClassName = "NovoPedidoItemBuilder", builderMethodName = "novoPedidoItemBuilder")
    private static PedidoItem novoPedidoItem(PedidoId pedidoId, Produto produto, Quantidade qtde) {

        Objects.requireNonNull(produto);
        Objects.requireNonNull(pedidoId);
        Objects.requireNonNull(qtde);

        PedidoItem pedidoItem = new PedidoItem(
            new PedidoItemId(), pedidoId, produto.id(), produto.nome(),
                produto.preco(), qtde, Moeda.ZERO
        );

        pedidoItem.recalcularTotal();
        return pedidoItem;
    }

    // métodos para alterar ações na classe
    // esse método só será usado dentro desse pacote (models)
    void alterarQtde(Quantidade quantid) {
        Objects.requireNonNull(quantid);
        this.setQtde(quantid);
        this.recalcularTotal();
    }

    private void recalcularTotal() {
        this.setValorTotal(this.getPreco().multiplicar(this.getQtde()));
    }

    // getters
    public PedidoItemId getPedidoItemId() {
        return pedidoItemId;
    }

    public PedidoId getPedidoId() {
        return pedidoId;
    }

    public ProdutoId getProdutoId() {
        return produtoId;
    }

    public ProdutoNome getProdutoNome() {
        return produtoNome;
    }

    public Moeda getPreco() {
        return preco;
    }

    public Quantidade getQtde() {
        return qtde;
    }

    public Moeda getValorTotal() {
        return valorTotal;
    }

    // setters - private
    private void setPedidoItemId(PedidoItemId pedidoItemId) {
        Objects.requireNonNull(pedidoItemId);
        this.pedidoItemId = pedidoItemId;
    }

    private void setPedidoId(PedidoId pedidoId) {
        Objects.requireNonNull(pedidoId);
        this.pedidoId = pedidoId;
    }

    private void setProdutoId(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);
        this.produtoId = produtoId;
    }

    private void setProdutoNome(ProdutoNome produtoNome) {
        Objects.requireNonNull(produtoNome);
        this.produtoNome = produtoNome;
    }

    private void setPreco(Moeda preco) {
        Objects.requireNonNull(preco);
        this.preco = preco;
    }

    private void setQtde(Quantidade qtde) {
        Objects.requireNonNull(qtde);
        this.qtde = qtde;
    }

    private void setValorTotal(Moeda valorTotal) {
        Objects.requireNonNull(valorTotal);
        this.valorTotal = valorTotal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pedidoItemId == null) ? 0 : pedidoItemId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PedidoItem other = (PedidoItem) obj;
        if (pedidoItemId == null) {
            if (other.pedidoItemId != null)
                return false;
        } else if (!pedidoItemId.equals(other.pedidoItemId))
            return false;
        return true;
    }   
}
