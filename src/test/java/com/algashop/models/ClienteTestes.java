package com.algashop.models;

import java.time.OffsetDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.exceptions.ClienteArquivadoException;
import com.algashop.domain.models.Cliente_3;
import com.algashop.domain.models.Cliente_4;
import com.algashop.domain.utils.UUIDGenerator;

public class ClienteTestes {
    
    @Test
    void testeCadastroClienteComEmailInvalido() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                new Cliente_3(
                    UUIDGenerator.generateTimeBaseUuid(),
                    "Fulano da Silva Sauro",
                    "inválido",
                    "123.456.789-01",
                    false,
                    OffsetDateTime.now()
                );
            });
    }

    @Test
    void testeAtualizarClienteComEmailValido() {

        Cliente_3 cliente = new Cliente_3(
            UUIDGenerator.generateTimeBaseUuid(),
            "Fulano da Silva Sauro",
            "fulano.sauro@email.com",
            "123.456.789-01",
            false,
            OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                cliente.alterarEmail("inválido");
            });

        //cliente.alterarEmail("inválido");
    }

    @Test
    void arquivarCliente() {
        Cliente_4 cliente = new Cliente_4(
            UUIDGenerator.generateTimeBaseUuid(),
            "Fulano da Silva Sauro",
            "fulano.sauro@email.com",
            "123.456.789-01",
            false,
            OffsetDateTime.now()
        );

        cliente.arquivar();
        Assertions.assertWith(cliente, 
            c -> Assertions.assertThat(c.nome()).isEqualTo("Anonymous"),
            c -> Assertions.assertThat(c.email()).isNotEqualTo("fulano.sauro@email.com"),
            c -> Assertions.assertThat(c.cpf()).isEqualTo("000.000.000-00"),
            c -> Assertions.assertThat(c.nascimento()).isNull(),
            c -> Assertions.assertThat(c.notificacoesPromocoesPermitidas()).isFalse());
    }

    @Test
    void tentarArquivarClienteJaArquivado() {
        Cliente_4 cliente = new Cliente_4(
            UUIDGenerator.generateTimeBaseUuid(),
            "Fulano da Silva Sauro",
            "fulano.sauro@email.com",
            "123.456.789-01",
            false,
            OffsetDateTime.now()
        );

        cliente.arquivar();

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(cliente::arquivar);

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.alterarEmail("email.alterado@email.com"));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.alterarNome("Outro Nome da Silva Sauro"));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.alterarTelefone("(00) 1110-2220"));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.habilitarNotificacoes());

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
            .isThrownBy(() -> cliente.desabilitarNotificacoes());
    }

    @Test
    void arquivarClienteAdicionarPontos() {
        Cliente_4 cliente = new Cliente_4(
            UUIDGenerator.generateTimeBaseUuid(),
            "Fulano da Silva Sauro",
            null, 
            "fulano.sauro@email.com",
            "(00) 0001-002",
            "123.456.789-01", 
            false,
            false, 
            OffsetDateTime.now(),
            null, 
            0
        );

        cliente.adicionarPontos(15);
        cliente.adicionarPontos(12);

        Assertions.assertThat(cliente.pontosFidelidade()).isEqualTo(27);
    }

    @Test
    void arquivarClienteAdicionarPontosNegativosGeraException() {
        Cliente_4 cliente = new Cliente_4(
            UUIDGenerator.generateTimeBaseUuid(),
            "Fulano da Silva Sauro",
            null, 
            "fulano.sauro@email.com",
            "(00) 0001-002",
            "123.456.789-01", 
            false,
            false, 
            OffsetDateTime.now(),
            null, 
            0
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> cliente.adicionarPontos(-10));
    }
}
