package com.algashop.domain.models;

import java.util.Objects;

import com.algashop.domain.exceptions.CarrinhoComprasItemIncompativelException;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.ProdutoNome;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.CarrinhoComprasId;
import com.algashop.domain.valueObjects.id.CarrinhoComprasItemId;
import com.algashop.domain.valueObjects.id.ProdutoId;

import lombok.Builder;

public class CarrinhoComprasItem {

    private CarrinhoComprasItemId carrinhoComprasItemId;
    private CarrinhoComprasId carrinhoComprasId;
    private ProdutoId produtoId;
    private ProdutoNome nome;
    private Moeda preco;
    private Quantidade qtde;
    private Boolean disponivel;
    private Moeda valorTotal;

    @Builder(builderClassName = "CarrinhoComprasItemBuilder", builderMethodName = "itemExistente")
    public CarrinhoComprasItem(CarrinhoComprasItemId carrinhoComprasItemId, CarrinhoComprasId carrinhoComprasId,
            ProdutoId produtoId, ProdutoNome nome, Moeda preco, Quantidade qtde, Boolean disponivel, Moeda valorTotal) {
        this.setCarrinhoComprasItemId(carrinhoComprasItemId);
        this.setCarrinhoComprasId(carrinhoComprasId);
        this.setProdutoId(produtoId);
        this.setNome(nome);
        this.setPreco(preco);
        this.setQtde(qtde);
        this.setDisponivel(disponivel);
        this.setValorTotal(valorTotal);
    }

    @Builder(builderClassName = "NovoItemCarrinhoBuilder", builderMethodName = "novoItemCarrinho")
    public CarrinhoComprasItem(CarrinhoComprasId carrinhoComprasId, ProdutoId produtoId,
        ProdutoNome produtoNome, Moeda preco, Quantidade quantidade, Boolean disponivel) {
        
        this(new CarrinhoComprasItemId(), carrinhoComprasId, produtoId, produtoNome, preco, quantidade, disponivel, Moeda.ZERO);
        this.recalcularTotal();
    }

    public void atualizar(Produto produto) {
        Objects.requireNonNull(produto);
        Objects.requireNonNull(produto.id());

        if (!produto.id().equals(this.getProdutoId())) {
            throw new CarrinhoComprasItemIncompativelException(this.getCarrinhoComprasItemId(), this.getProdutoId());
        }

        this.setPreco(produto.preco());
        this.setDisponivel(produto.temEstoque());
        this.setNome(produto.nome());
        this.recalcularTotal();
    }

    public void alterarQuantidade(Quantidade quantidade) {
        this.setQtde(quantidade);
        this.recalcularTotal();
    }

    public CarrinhoComprasItemId getCarrinhoComprasItemId() {
        return carrinhoComprasItemId;
    }

    public CarrinhoComprasId getCarrinhoComprasId() {
        return carrinhoComprasId;
    }

    public ProdutoId getProdutoId() {
        return produtoId;
    }

    public ProdutoNome getNome() {
        return nome;
    }

    public Moeda getPreco() {
        return preco;
    }

    public Quantidade getQtde() {
        return qtde;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public Moeda getValorTotal() {
        return valorTotal;
    }

    private void setCarrinhoComprasItemId(CarrinhoComprasItemId carrinhoComprasItemId) {
        Objects.requireNonNull(carrinhoComprasItemId);
        this.carrinhoComprasItemId = carrinhoComprasItemId;
    }

    private void setCarrinhoComprasId(CarrinhoComprasId carrinhoComprasId) {
        Objects.requireNonNull(carrinhoComprasId);
        this.carrinhoComprasId = carrinhoComprasId;
    }

    private void setProdutoId(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);
        this.produtoId = produtoId;
    }

    private void setNome(ProdutoNome nome) {
        Objects.requireNonNull(nome);
        this.nome = nome;
    }

    private void setPreco(Moeda preco) {
        Objects.requireNonNull(preco);
        this.preco = preco;
    }

    private void setQtde(Quantidade qtde) {
        Objects.requireNonNull(qtde);

        if (qtde.equals(Quantidade.ZERO)) {
            throw new IllegalArgumentException();
        }

        this.qtde = qtde;
    }

    private void setDisponivel(Boolean disponivel) {
        Objects.requireNonNull(disponivel);
        this.disponivel = disponivel;
    }

    private void setValorTotal(Moeda valorTotal) {
        Objects.requireNonNull(valorTotal);
        this.valorTotal = valorTotal;
    }

    private void recalcularTotal() {
        this.setValorTotal(preco.multiplicar(qtde));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((carrinhoComprasItemId == null) ? 0 : carrinhoComprasItemId.hashCode());
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
        CarrinhoComprasItem other = (CarrinhoComprasItem) obj;
        if (carrinhoComprasItemId == null) {
            if (other.carrinhoComprasItemId != null)
                return false;
        } else if (!carrinhoComprasItemId.equals(other.carrinhoComprasItemId))
            return false;
        return true;
    }
}
