package com.algashop.models;

import java.time.OffsetDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.exceptions.ClienteArquivadoException;
import com.algashop.domain.models.Cliente;
import com.algashop.domain.valueObjects.ClienteCEP;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEmail;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteId;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClientePontosFidelidade;
import com.algashop.domain.valueObjects.ClienteTelefone;

public class ClienteTestes_1 {
    
    @Test
    void testeCadastroClienteComEmailInvalido() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                new Cliente(
                    //UUIDGenerator.generateTimeBaseUuid(),
                    new ClienteId(),
                    //"Fulano da Silva Sauro",
                    new ClienteNome("Fulano ", "da Silva Sauro"),
                    new ClienteEmail("email-inválido"),
                    new ClienteCPF("123.456.789-01"),
                    false,
                    OffsetDateTime.now(),
                    ClienteEndereco.builder()
                        .logradouro("Rua Cinco de Outubro, 997")
                        .bairro("Vila Norte")
                        .cidade("Água Branca")
                        .estado("Goiás")
                        .cep(new ClienteCEP("12345678"))
                        .build()
                );
            });
    }

    @Test
    void testeAtualizarClienteComEmailValido() {

        Cliente cliente = new Cliente(
            //UUIDGenerator.generateTimeBaseUuid(),
            new ClienteId(),
            //"Fulano da Silva Sauro",
            new ClienteNome("Fulano ", "da Silva Sauro"),
            //"fulano.sauro@email.com",
            new ClienteEmail("fulano.sauro@email.com"),
            //"123.456.789-01",
            new ClienteCPF("123.456.789-01"),
            false,
            OffsetDateTime.now(),
            ClienteEndereco.builder()
                .logradouro("Rua Cinco de Outubro, 997")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                cliente.alterarEmail(new ClienteEmail("inválido"));
            });

        //cliente.alterarEmail("inválido");
    }

    @Test
    void arquivarCliente() {
        Cliente cliente = new Cliente(
            //UUIDGenerator.generateTimeBaseUuid(),
            new ClienteId(),
            //"Fulano da Silva Sauro",
            new ClienteNome("Fulano ", "da Silva Sauro"),
            //"fulano.sauro@email.com",
            new ClienteEmail("fulano.sauro@email.com"),
            //"123.456.789-01",
            new ClienteCPF("123.456.789-01"),
            false,
            OffsetDateTime.now(),
            ClienteEndereco.builder()
                .logradouro("Rua Cinco de Outubro, 997")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
        );

        cliente.arquivar();

        Assertions.assertWith(cliente, 
            c -> Assertions.assertThat(c.nome()).isEqualTo(new ClienteNome("Anonymous", "Anonymous")),
            c -> Assertions.assertThat(c.email()).isNotEqualTo(new ClienteEmail("fulano.sauro@email.com")),
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
        Cliente cliente = new Cliente(
            //UUIDGenerator.generateTimeBaseUuid(),
            new ClienteId(),
            //"Fulano da Silva Sauro",
            new ClienteNome("Fulano ", "da Silva Sauro"),
            //"fulano.sauro@email.com",
            new ClienteEmail("fulano.sauro@email.com"),
            //"123.456.789-01",
            new ClienteCPF("123.456.789-01"),
            false,
            OffsetDateTime.now(),
            ClienteEndereco.builder()
                .logradouro("Rua Cinco de Outubro, 997")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
        );

        cliente.arquivar();

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
        Cliente cliente = new Cliente(
            //UUIDGenerator.generateTimeBaseUuid(),
            new ClienteId(),
            //"Fulano da Silva Sauro",
            new ClienteNome("Fulano ", "da Silva Sauro"),
            null, 
            //"fulano.sauro@email.com",
            new ClienteEmail("fulano.sauro@email.com"),
            new ClienteTelefone("(00) 0001-002"),
            //"123.456.789-01",
            new ClienteCPF("123.456.789-01"),
            false,
            false, 
            OffsetDateTime.now(),
            null, 
            new ClientePontosFidelidade(10),
            ClienteEndereco.builder()
                .logradouro("Rua Cinco de Outubro, 997")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
        );

        cliente.adicionarPontos(new ClientePontosFidelidade(15));
        cliente.adicionarPontos(new ClientePontosFidelidade(12));

        Assertions.assertThat(cliente.pontosFidelidade()).isEqualTo(new ClientePontosFidelidade(37));
    }

    @Test
    void arquivarClienteAdicionarPontosNegativosGeraException() {
        Cliente cliente = new Cliente(
            //UUIDGenerator.generateTimeBaseUuid(),
            new ClienteId(),
            //"Fulano da Silva Sauro",
            new ClienteNome("Fulano ", "da Silva Sauro"),
            null,
            //"fulano.sauro@email.com",
            new ClienteEmail("fulano.sauro@email.com"),
            //"(00) 0001-002",
            new ClienteTelefone("(00) 0001-002"),
            //"123.456.789-01",
            new ClienteCPF("123.456.789-01"),
            false,
            false, 
            OffsetDateTime.now(),
            null, 
            new ClientePontosFidelidade(0),
            ClienteEndereco.builder()
                .logradouro("Rua Cinco de Outubro, 997")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> cliente.adicionarPontos(new ClientePontosFidelidade(-10)));
    }
}
