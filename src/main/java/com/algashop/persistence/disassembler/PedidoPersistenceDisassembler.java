package com.algashop.persistence.disassembler;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.algashop.domain.enums.FormasPagamento;
import com.algashop.domain.enums.PedidoStatus;
import com.algashop.domain.models.Pedido;
import com.algashop.domain.valueObjects.ClienteCEP;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClienteTelefone;
import com.algashop.domain.valueObjects.Destinatario;
import com.algashop.domain.valueObjects.InformacoesCobranca;
import com.algashop.domain.valueObjects.InformacoesEntrega;
import com.algashop.domain.valueObjects.Moeda;
import com.algashop.domain.valueObjects.Quantidade;
import com.algashop.domain.valueObjects.id.ClienteId;
import com.algashop.domain.valueObjects.id.PedidoId;
import com.algashop.persistence.embeddable.DestinatarioEmbeddable;
import com.algashop.persistence.embeddable.EnderecoEmbeddable;
import com.algashop.persistence.embeddable.EntregaEmbeddable;
import com.algashop.persistence.embeddable.FaturamentoEmbeddable;
import com.algashop.persistence.entity.PedidoPersistenceEntity;

@Component
public class PedidoPersistenceDisassembler {
    
    public Pedido paraClasseDominio(PedidoPersistenceEntity persistenceEntity) {
        return Pedido.pedidoExistenteBuilder()
            .pedidoId(new PedidoId(persistenceEntity.getId()))
            .clienteId(new ClienteId(persistenceEntity.getClienteId()))
            .valorTotal(new Moeda(persistenceEntity.getValorTotal()))
            .qtdeTotal(new Quantidade(persistenceEntity.getQtdeTotalItens()))
            .statusPedido(PedidoStatus.valueOf(persistenceEntity.getStatus()))
            .formasPagamento(FormasPagamento.valueOf(persistenceEntity.getFormaPagamento()))
            .feitoEm(persistenceEntity.getCriadoEm())
            .pagoEm(persistenceEntity.getPagoEm())
            .canceladoEm(persistenceEntity.getCanceladoEm())
            .finalizadoEm(persistenceEntity.getFinalizadoEm())
            .itens(new HashSet<>())
            .versaoPedido(persistenceEntity.getVersaoPedido())
            .build();
    }

    private InformacoesEntrega paraEntregaObjectValue(EntregaEmbeddable entregaEmbeddable) {
        DestinatarioEmbeddable destinatario = entregaEmbeddable.getCliente();

        return InformacoesEntrega.builder()
            .valorEntrega(new Moeda(entregaEmbeddable.getValorEntrega()))
            .previsaoEntrega(entregaEmbeddable.getPrevisaoEntrega())
            .cliente(
                Destinatario.builder()
                    .nome(new ClienteNome(destinatario.getNome(), destinatario.getSobrenome()))
                    .cpf(new ClienteCPF(destinatario.getCpf()))
                    .telefone(new ClienteTelefone(destinatario.getTelefone()))
                    .build()
            )
            .endereco(paraEnderecoObjectValue(entregaEmbeddable.getEndereco()))
            .build();
    }

    private InformacoesCobranca paraFaturamentoObjectValue(FaturamentoEmbeddable faturamento) {
        return InformacoesCobranca.builder()
            .nome(new ClienteNome(faturamento.getNome(), faturamento.getSobrenome()))
            .cpf(new ClienteCPF(faturamento.getCpf()))
            .telefone(new ClienteTelefone(faturamento.getTelefone()))
            .endereco(paraEnderecoObjectValue(faturamento.getEndereco()))
            .build();
    }

    private ClienteEndereco paraEnderecoObjectValue(EnderecoEmbeddable enderecoEmbeddable) {
        return ClienteEndereco.builder()
            .cep(new ClienteCEP(enderecoEmbeddable.getCep()))
            .logradouro(enderecoEmbeddable.getLogradouro())
            .complemento(enderecoEmbeddable.getComplemento())
            .bairro(enderecoEmbeddable.getBairro())
            .cidade(enderecoEmbeddable.getCidade())
            .estado(enderecoEmbeddable.getEstado())
            .build();
    }
}
