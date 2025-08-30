package com.algashop.domain.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.exceptions.DataEntregaInvalidaException;
import com.algashop.domain.exceptions.PedidoNaoContemItemException;
import com.algashop.domain.exceptions.PedidoNaoFeitoException;
import com.algashop.domain.exceptions.StatusPedidoNaoPodeAlterarException;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Produto;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.domain.valueObjects.id.PedidoItemId;

import lombok.Builder;

public class Pedido {

    private PedidoId pedidoId;
    private ClienteId clienteId;
    private Moeda valorTotal;
    private Quantidade qtdeTotal;
    private OffsetDateTime feitoEm; // placedAt
    private OffsetDateTime pagoEm; // paidAt
    private OffsetDateTime canceladoEm;
    private OffsetDateTime finalizadoEm;
    private InformacoesEntrega entrega; // shipping
    private InformacoesCobranca faturamento; // billing
    private PedidoStatus statusPedido;
    private FormasPagamento formasPagamento;
    //private Moeda valorEntrega;
    //private LocalDate previsaoEntrega;
    private Set<PedidoItem> itens;

    // construtor com todos os parâmetros
    @Builder(builderClassName = "PedidoExistenteBuilder", builderMethodName = "pedidoExistenteBuilder")
    public Pedido(PedidoId pedidoId, ClienteId clienteId, Moeda valorTotal, 
        Quantidade qtdeTotal, OffsetDateTime feitoEm, OffsetDateTime pagoEm, 
        OffsetDateTime canceladoEm, OffsetDateTime finalizadoEm, InformacoesEntrega entrega,
        InformacoesCobranca faturamento, PedidoStatus statusPedido, 
        FormasPagamento formasPagamento, Set<PedidoItem> itens) {

        this.setPedidoId(pedidoId);
        this.setClienteId(clienteId);
        this.setValorTotal(valorTotal);
        this.setQtdeTotal(qtdeTotal);
        this.setFeitoEm(feitoEm);
        this.setPagoEm(pagoEm);
        this.setCanceladoEm(canceladoEm);
        this.setFinalizadoEm(finalizadoEm);
        this.setEntrega(entrega);
        this.setFaturamento(faturamento);
        this.setStatusPedido(statusPedido);
        this.setFormasPagamento(formasPagamento);
        //this.setValorEntrega(valorEntrega);
        //this.setPrevisaoEntrega(previsaoEntrega);
        this.setItens(itens);
    }

    // factory
    public static Pedido rascunhoPedido(ClienteId clienteId) {

        return new Pedido(
            new PedidoId(), clienteId, Moeda.ZERO, Quantidade.ZERO, null, null, 
                null, null, null, null, PedidoStatus.RASCUNHO, null,
                new HashSet<>()
        );
    }

    // métodos para alterar ações na classe
    /*public void adicionarItem(ProdutoId produtoId, ProdutoNome produtoNome, 
        Moeda preco, Quantidade qtde) {*/
    public void adicionarItem(Produto produto, Quantidade qtde) {

        Objects.requireNonNull(produto);
        Objects.requireNonNull(qtde);

        produto.verificarSeTemEstoque();
        
        PedidoItem pedidoItem = PedidoItem.novoPedidoItemBuilder()
            .pedidoId(this.pedidoId)
            .qtde(qtde)
            .produto(produto)
            .build();

        // se a lista estiver vazia, instancia new HashSet para não correr risco de nullpointerexception
        if (this.itens == null) {
            this.itens = new HashSet<>();
        }

        this.itens.add(pedidoItem);
        this.recalcularTotal();
    }

    public void alterarQtdeItens(PedidoItemId itemId, Quantidade qtde) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(qtde);

        PedidoItem itemPedido = this.buscarItemPedido(itemId);
        itemPedido.alterarQtde(qtde);
        this.recalcularTotal();
    }

    public void confirmarPedido() {
        /*Objects.requireNonNull(this.getEntrega());
        Objects.requireNonNull(this.getFaturamento());
        Objects.requireNonNull(this.getPrevisaoEntrega());
        Objects.requireNonNull(this.getValorEntrega());
        Objects.requireNonNull(this.getFormasPagamento());
        Objects.requireNonNull(this.getItens());*/

        if (this.getItens().isEmpty()) {
            this.verificarSePodeAlterarParaPedidoConfirmado();
        }

        // altera antes de definir a data
        this.alterarStatus(PedidoStatus.FEITO);
        this.setFeitoEm(OffsetDateTime.now());
    }

    public void pedidoPago() {
        this.setPagoEm(OffsetDateTime.now());
        this.alterarStatus(PedidoStatus.PAGO);
    }

    public void alterarFormaPagamento(FormasPagamento formaPgto) {
        Objects.requireNonNull(formaPgto);
        this.setFormasPagamento(formaPgto);
    }

    public void alterarInfoFaturamento(InformacoesCobranca infoFaturamento) {
        Objects.requireNonNull(infoFaturamento);
        this.setFaturamento(infoFaturamento);
    }

    public void alterarEntrega(InformacoesEntrega novaEntrega) {
        Objects.requireNonNull(novaEntrega);
        //Objects.requireNonNull(valor);
        //Objects.requireNonNull(data);

        if (novaEntrega.previsaoEntrega().isBefore(LocalDate.now())) {
            throw new DataEntregaInvalidaException(this.getPedidoId());
        }

        this.setEntrega(novaEntrega);
        this.recalcularTotal();
        //this.setValorEntrega(valor);
        //this.setPrevisaoEntrega(data);
    }

    public boolean estaRascunho() {
        return PedidoStatus.RASCUNHO.equals(this.getStatusPedido());
    }

    public boolean estaFeito() {
        return PedidoStatus.FEITO.equals(this.getStatusPedido());
    }

    public boolean estaPronto() {
        return PedidoStatus.PRONTO.equals(this.getStatusPedido());
    }

    public boolean estaPago() {
        return PedidoStatus.PAGO.equals(this.getStatusPedido());
    }

    private void recalcularTotal() {
        BigDecimal valorTotalItens = this.getItens().stream()
            .map(i -> i.getValorTotal().valor())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer qtdeTotalItens = this.getItens().stream()
            .map(i -> i.getQtde().valor())
            .reduce(0, Integer::sum);

        BigDecimal valorEntrega;
        if (this.getEntrega() == null) {
            valorEntrega = BigDecimal.ZERO;
        } else {
            valorEntrega = this.getEntrega().valorEntrega().valor();
        }

        BigDecimal valorTotal = valorTotalItens.add(valorEntrega);
        // faz nova redefinição dos setters
        this.setValorTotal(new Moeda(valorTotal));
        this.setQtdeTotal(new Quantidade(qtdeTotalItens));
    }

    private void alterarStatus(PedidoStatus novoStatus) {
        Objects.requireNonNull(novoStatus);

        if (this.getStatusPedido().naoPodeAlterarStatus(novoStatus)) {
            throw new StatusPedidoNaoPodeAlterarException(this.getPedidoId(), this.getStatusPedido(), novoStatus);
        }

        this.setStatusPedido(novoStatus);
    }

    private void verificarSePodeAlterarParaPedidoConfirmado() {
        if (this.getItens() == null || this.getItens().isEmpty()) {
            throw PedidoNaoFeitoException.semItens(pedidoId);
        }

        if (this.getEntrega() == null) {
            throw PedidoNaoFeitoException.semInformacoesEntrega(pedidoId);
        }

        /*if (this.getValorEntrega() == null) {
            throw PedidoNaoFeitoException.valorEntregaInvalido(pedidoId);
        }

        if (this.getPrevisaoEntrega() == null) {
            throw PedidoNaoFeitoException.previsaoEntregaInvalida(pedidoId);
        }*/

        if (this.getFaturamento() == null) {
            throw PedidoNaoFeitoException.semInformacoesCobranca(pedidoId);
        }

        if (this.getFormasPagamento() == null) {
            throw PedidoNaoFeitoException.metodoPagamentoInvalido(pedidoId);
        }
    }

    private PedidoItem buscarItemPedido(PedidoItemId itemId) {
        Objects.requireNonNull(itemId);
        return this.getItens().stream()
            .filter(i -> i.getPedidoItemId().equals(itemId)).findFirst()
            .orElseThrow(() -> new PedidoNaoContemItemException(this.getPedidoId(), itemId));
    }

    // getters
    public PedidoId getPedidoId() {
        return pedidoId;
    }

    public ClienteId getClienteId() {
        return clienteId;
    }

    public Moeda getValorTotal() {
        return valorTotal;
    }

    public Quantidade getQtdeTotal() {
        return qtdeTotal;
    }

    public OffsetDateTime getFeitoEm() {
        return feitoEm;
    }

    public OffsetDateTime getPagoEm() {
        return pagoEm;
    }

    public OffsetDateTime getCanceladoEm() {
        return canceladoEm;
    }

    public OffsetDateTime getFinalizadoEm() {
        return finalizadoEm;
    }

    public InformacoesEntrega getEntrega() {
        return entrega;
    }

    public InformacoesCobranca getFaturamento() {
        return faturamento;
    }

    public PedidoStatus getStatusPedido() {
        return statusPedido;
    }

    public FormasPagamento getFormasPagamento() {
        return formasPagamento;
    }

    /*public Moeda getValorEntrega() {
        return valorEntrega;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }*/

    // essa lista não poderá ser modificada
    public Set<PedidoItem> getItens() {
        //return itens;
        return Collections.unmodifiableSet(this.itens);
    }

    // setters - private
    private void setPedidoId(PedidoId pedidoId) {
        Objects.requireNonNull(pedidoId);
        this.pedidoId = pedidoId;
    }

    private void setClienteId(ClienteId clienteId) {
        Objects.requireNonNull(clienteId);
        this.clienteId = clienteId;
    }

    private void setValorTotal(Moeda valorTotal) {
        Objects.requireNonNull(valorTotal);
        this.valorTotal = valorTotal;
    }

    private void setQtdeTotal(Quantidade qtdeTotal) {
        Objects.requireNonNull(qtdeTotal);
        this.qtdeTotal = qtdeTotal;
    }

    private void setFeitoEm(OffsetDateTime feitoEm) {
        this.feitoEm = feitoEm;
    }

    private void setPagoEm(OffsetDateTime pagoEm) {
        this.pagoEm = pagoEm;
    }

    private void setCanceladoEm(OffsetDateTime canceladoEm) {
        this.canceladoEm = canceladoEm;
    }

    private void setFinalizadoEm(OffsetDateTime finalizadoEm) {
        this.finalizadoEm = finalizadoEm;
    }

    private void setEntrega(InformacoesEntrega entrega) {
        this.entrega = entrega;
    }

    public void setFaturamento(InformacoesCobranca faturamento) {
        this.faturamento = faturamento;
    }

    private void setStatusPedido(PedidoStatus statusPedido) {
        Objects.requireNonNull(statusPedido);
        this.statusPedido = statusPedido;
    }

    private void setFormasPagamento(FormasPagamento formasPagamento) {
        this.formasPagamento = formasPagamento;
    }

    /*private void setValorEntrega(Moeda valorEntrega) {
        this.valorEntrega = valorEntrega;
    }

    private void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }*/

    private void setItens(Set<PedidoItem> itens) {
        Objects.requireNonNull(itens);
        this.itens = itens;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pedidoId == null) ? 0 : pedidoId.hashCode());
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
        Pedido other = (Pedido) obj;
        if (pedidoId == null) {
            if (other.pedidoId != null)
                return false;
        } else if (!pedidoId.equals(other.pedidoId))
            return false;
        return true;
    }
}
