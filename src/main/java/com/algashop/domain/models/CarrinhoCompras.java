package com.algashop.domain.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.algashop.domain.exceptions.CarrinhoComprasNaoContemItemException;
import com.algashop.domain.exceptions.CarrinhoComprasNaoContemProdutoException;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.CarrinhoComprasId;
import com.algashop.domain.valueObjects.id.CarrinhoComprasItemId;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.ProdutoId;

import lombok.Builder;

public class CarrinhoCompras implements AggregateRoot<CarrinhoComprasId> {

    private CarrinhoComprasId id;
    private ClienteId clienteId;
    private Moeda valorTotal;
    private Quantidade qtdeItens;
    private OffsetDateTime criadoEm;
    private Set<CarrinhoComprasItem> itens;

    @Builder(builderClassName = "CarrinhoExistenteBuilder", builderMethodName = "carrinhoExistente")
    public CarrinhoCompras(CarrinhoComprasId carrinhoComprasId, ClienteId clienteId, Moeda valorTotal,
            Quantidade qtdeItens, OffsetDateTime criadoEm, Set<CarrinhoComprasItem> itens) {
        this.setCarrinhoComprasId(carrinhoComprasId);
        this.setClienteId(clienteId);
        this.setValorTotal(valorTotal);
        this.setQtdeItens(qtdeItens);
        this.setCriadoEm(criadoEm);
        this.setItens(itens);
    }

    // métodos para alteração de ações na classe
    public static CarrinhoCompras iniciarCompra(ClienteId clienteId) {
        return new CarrinhoCompras(new CarrinhoComprasId(), clienteId, Moeda.ZERO,
            Quantidade.ZERO, OffsetDateTime.now(), new HashSet<>());
    }

    public void adicionarItemCarrinho(Produto produto, Quantidade quantidade) {
        Objects.requireNonNull(produto);
        Objects.requireNonNull(quantidade);

        produto.verificarSeTemEstoque();

        CarrinhoComprasItem carrinhoItem = CarrinhoComprasItem.novoItemCarrinho()
            .carrinhoComprasId(this.getId())
            .produtoId(produto.id())
            .produtoNome(produto.nome())
            .preco(produto.preco())
            .disponivel(produto.temEstoque())
            .quantidade(quantidade)
            .build();

        buscarItemPorProduto(produto.id())
            .ifPresentOrElse(i -> atualizarItem(i, produto, quantidade), () -> adicionarItem(carrinhoItem));

        this.recalcularTotal();
    }

    public void removerItem(CarrinhoComprasItemId itemId) {
        CarrinhoComprasItem item = this.buscarItem(itemId);
        this.itens.remove(item);
        this.recalcularTotal();
    }

    public CarrinhoComprasItem buscarItem(CarrinhoComprasItemId itemId) {
        Objects.requireNonNull(itemId);
        return this.itens.stream()
                .filter(i -> i.getCarrinhoComprasItemId().equals(itemId)).findFirst()
                .orElseThrow(() -> new CarrinhoComprasNaoContemItemException(this.getId(), itemId));
    }

    public CarrinhoComprasItem buscarItem(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);
        return this.itens.stream()
                .filter(i -> i.getProdutoId().equals(produtoId)).findFirst()
                .orElseThrow(() -> new CarrinhoComprasNaoContemProdutoException(this.getId(), produtoId));
    }

    public void atualizarItem(Produto produto) {
        CarrinhoComprasItem carrinhoItem = this.buscarItem(produto.id());
        carrinhoItem.atualizar(produto);
        this.recalcularTotal();
    }

    public void alterarItemQuantidade(CarrinhoComprasItemId itemId, Quantidade quantidade) {
        CarrinhoComprasItem carrinhoItem = this.buscarItem(itemId);
        carrinhoItem.alterarQuantidade(quantidade);
        this.recalcularTotal();
    }

    public boolean contemItemIndisponivel() {
        return itens.stream().anyMatch(i -> !i.getDisponivel());
    }

    public void limparCarrinho() {
        itens.clear();
        valorTotal = Moeda.ZERO;
        qtdeItens = Quantidade.ZERO;
    }

    public boolean carrinhoEstaVazio() {
        return this.getItens().isEmpty();
    }

    // getters
    public CarrinhoComprasId getId() {
        return id;
    }

    public ClienteId getClienteId() {
        return clienteId;
    }

    public Moeda getValorTotal() {
        return valorTotal;
    }

    public Quantidade getQtdeItens() {
        return qtdeItens;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    // esse conjunto não poderá ser modificado
    public Set<CarrinhoComprasItem> getItens() {
        return Collections.unmodifiableSet(itens);
    }

    private void setCarrinhoComprasId(CarrinhoComprasId carrinhoComprasId) {
        Objects.requireNonNull(carrinhoComprasId);
        this.id = carrinhoComprasId;
    }

    private void setClienteId(ClienteId clienteId) {
        Objects.requireNonNull(clienteId);
        this.clienteId = clienteId;
    }

    private void setValorTotal(Moeda valorTotal) {
        Objects.requireNonNull(valorTotal);
        this.valorTotal = valorTotal;
    }

    private void setQtdeItens(Quantidade qtdeItens) {
        Objects.requireNonNull(qtdeItens);
        this.qtdeItens = qtdeItens;
    }

    private void setCriadoEm(OffsetDateTime criadoEm) {
        Objects.requireNonNull(criadoEm);
        this.criadoEm = criadoEm;
    }

    private void setItens(Set<CarrinhoComprasItem> itens) {
        Objects.requireNonNull(itens);
        this.itens = itens;
    }

    // métodos privados
    private void adicionarItem(CarrinhoComprasItem carrinhoComprasItem) {
        this.itens.add(carrinhoComprasItem);
    }

    private void atualizarItem(CarrinhoComprasItem carrinhoComprasItem, Produto produto, Quantidade quantidade) {
        carrinhoComprasItem.atualizar(produto);
        carrinhoComprasItem.alterarQuantidade(carrinhoComprasItem.getQtde().add(quantidade));
    }

    private Optional<CarrinhoComprasItem> buscarItemPorProduto(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);

        return this.itens.stream()
            .filter(i -> i.getProdutoId().equals(produtoId))
            .findFirst();
    }

    private void recalcularTotal() {
        BigDecimal valorTotalItens = itens.stream()
                .map(i -> i.getValorTotal().valor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer qtdTotalItens = itens.stream()
                .map(i -> i.getQtde().valor())
                .reduce(0, Integer::sum);

        this.valorTotal = new Moeda(valorTotalItens);
        this.qtdeItens = new Quantidade(qtdTotalItens);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        CarrinhoCompras other = (CarrinhoCompras) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
