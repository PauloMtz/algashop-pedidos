package com.algashop.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.algashop.domain.models.Cliente;
import com.algashop.domain.valueObjects.ClienteCEP;
import com.algashop.domain.valueObjects.ClienteCPF;
import com.algashop.domain.valueObjects.ClienteEmail;
import com.algashop.domain.valueObjects.ClienteEndereco;
import com.algashop.domain.valueObjects.ClienteNascimento;
import com.algashop.domain.valueObjects.ClienteNome;
import com.algashop.domain.valueObjects.ClientePontosFidelidade;
import com.algashop.domain.valueObjects.ClienteTelefone;
import com.algashop.domain.valueObjects.id.ClienteId;

public class ClienteTestesDataBuilder {
    
    private ClienteTestesDataBuilder() {}

    public static Cliente.NovoClienteBuilder novoCliente() {

        return Cliente.novoCliente()
            .nome(new ClienteNome("Fulano", "de Tal"))
            .nascimento(new ClienteNascimento(LocalDate.of(1991, 12, 7)))
            .email(new ClienteEmail("fulano@email.com"))
            .telefone(new ClienteTelefone("(00) 0001-0002"))
            .cpf(new ClienteCPF("123.456.789-01"))
            .notificacoesPromocoesPermitidas(false)
            .endereco(ClienteEndereco.builder()
                .logradouro("Rua Cinco de Outubro, 997")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
            );
    }

    public static Cliente.ClienteExistenteBuilder clienteExistente() {

        return Cliente.clienteExistenteBuilder()
            .id(new ClienteId())
            .nome(new ClienteNome("Fulano", "de Tal"))
            .nascimento(new ClienteNascimento(LocalDate.of(1991, 12, 7)))
            .email(new ClienteEmail("fulano@email.com"))
            .telefone(new ClienteTelefone("(01) 0001-0002"))
            .cpf(new ClienteCPF("123.456.789-01"))
            .notificacoesPromocoesPermitidas(false)
            .arquivado(false)
            .cadastradoEm(OffsetDateTime.now())
            .arquivadoEm(null)
            .pontosFidelidade(new ClientePontosFidelidade(10))
            .endereco(ClienteEndereco.builder()
                .logradouro("Rua Cinco de Outubro, 997")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
            );
    }

    public static Cliente.ClienteExistenteBuilder clienteAnonimizado() {

        return Cliente.clienteExistenteBuilder()
            .id(new ClienteId())
            .nome(new ClienteNome("Cliente", "Anônimo"))
            .nascimento(new ClienteNascimento(LocalDate.of(1991, 12, 7)))
            .email(new ClienteEmail("email@anonimo.com"))
            .telefone(new ClienteTelefone("(00) 0000-0000"))
            .cpf(new ClienteCPF("000.000.000-00"))
            .notificacoesPromocoesPermitidas(false)
            .arquivado(false) // alterar conforme o teste
            .cadastradoEm(OffsetDateTime.now())
            .arquivadoEm(OffsetDateTime.now())
            .pontosFidelidade(new ClientePontosFidelidade(10))
            .endereco(ClienteEndereco.builder()
                .logradouro("Anonymous")
                .bairro("Vila Norte")
                .cidade("Água Branca")
                .estado("Goiás")
                .cep(new ClienteCEP("12345678"))
                .build()
            );
    }
}
