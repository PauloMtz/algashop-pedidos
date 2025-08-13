package com.algashop.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.exceptions.ClienteArquivadoException;
import com.algashop.domain.models.Cliente;
import com.algashop.domain.valueObjects.ClienteCEP;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEmail;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClientePontosFidelidade;
import com.algashop.domain.valueObjects.ClienteTelefone;

public class ClienteTestes {
    @Test
    void testeCadastroClienteComEmailInvalido() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> ClienteTestesDataBuilder.novoCliente().email(new ClienteEmail("email-inválido")).build());
    }

    @Test
    void testeAtualizarClienteComEmailValido() {
        Cliente cliente = ClienteTestesDataBuilder.novoCliente().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                cliente.alterarEmail(new ClienteEmail("inválido"));
            });
    }

    @Test
    void arquivarCliente() {
        // neste teste, arquivado deve estar false
        Cliente cliente = ClienteTestesDataBuilder.clienteAnonimizado().build();

        cliente.arquivar();

        Assertions.assertWith(cliente, 
            c -> Assertions.assertThat(c.nome()).isEqualTo(new ClienteNome("Cliente", "Anônimo")),
            c -> Assertions.assertThat(c.email()).isNotEqualTo(new ClienteEmail("email@anonimo.com")),
            c -> Assertions.assertThat(c.cpf()).isEqualTo(new ClienteCPF("000.000.000-00")),
            c -> Assertions.assertThat(c.nascimento()).isNull(),
            c -> Assertions.assertThat(c.notificacoesPromocoesPermitidas()).isFalse(),
            c -> Assertions.assertThat(c.getEndereco()).isEqualTo(
                    ClienteEndereco.builder()
                        .logradouro("Anonymous")
                        .complemento(null)
                        .bairro("Vila Norte")
                        .cidade("Água Branca")
                        .estado("Goiás")
                        .cep(new ClienteCEP("12345678"))
                        .build()
                )
            );
    }

    @Test
    void tentarArquivarClienteJaArquivado() {
        // neste teste, arquivado deve estar true
        Cliente cliente = ClienteTestesDataBuilder.clienteAnonimizado().build();

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(cliente::arquivar);

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.alterarEmail(new ClienteEmail("email.alterado@email.com")));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.alterarNome(new ClienteNome("Outro Nome", "da Silva Sauro")));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.alterarTelefone(new ClienteTelefone("(00) 1110-2220")));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.habilitarNotificacoes());

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.desabilitarNotificacoes());
    }

    @Test
    void arquivarClienteAdicionarPontos() {
        Cliente cliente = ClienteTestesDataBuilder.clienteExistente().build();

        cliente.adicionarPontos(new ClientePontosFidelidade(15));
        cliente.adicionarPontos(new ClientePontosFidelidade(12));

        Assertions.assertThat(cliente.pontosFidelidade()).isEqualTo(new ClientePontosFidelidade(37));
    }

    @Test
    void arquivarClienteAdicionarPontosNegativosGeraException() {
        Cliente cliente = ClienteTestesDataBuilder.clienteExistente().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> cliente.adicionarPontos(new ClientePontosFidelidade(-10)));
    }
}
