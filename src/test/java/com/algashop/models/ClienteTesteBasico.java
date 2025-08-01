package com.algashop.models;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.algashop.domain.models.Cliente_1;
import com.algashop.domain.models.Cliente_2;
import com.algashop.domain.utils.UUIDGenerator;

public class ClienteTesteBasico {

    /*
     * reclamou desse tipo de abordagem do java bean até babar
     * permite tudo, sei lá o quê, mais não sei o quê, trá lá lá... sei lá...
     */
    
    @Test
    public void testeCliente_1() {
        Cliente_1 cliente = new Cliente_1();
        cliente.setId(UUID.randomUUID());
        cliente.setNome("Fulano da Silva Sauro");
        cliente.setCpf("1234");
        cliente.setPontosFidelidade(10);
    }

    // teste básico da entidade rich model
    @Test
    public void testeCliente_2() {
        Cliente_2 cliente = new Cliente_2(
            //UUID.randomUUID(),
            UUIDGenerator.generateTimeBaseUuid(),
            "Fulano da Silva Sauro",
            "fulano.sauro@email.com",
            "123.456.789-01",
            true,
            OffsetDateTime.now()
        );

        System.out.println(cliente.id());
        System.out.println(UUIDGenerator.generateTimeBaseUuid());

        // saída no console
        //01985deb-0909-7128-a7bc-375b24fbeb3e
        //01985deb-0994-7e39-81a7-e628a4700d0a

        cliente.adicionarPontos(10);
    }
}
