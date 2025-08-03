package com.algashop.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algashop.domain.valueObjects.ClientePontosFidelidade;

public class ClientePontosFidelidadeTestes {
    
    @Test
    void adicionarPontos() {
        ClientePontosFidelidade pontos = new ClientePontosFidelidade(7);
        Assertions.assertThat(pontos.valor()).isEqualTo(7);
    }

    @Test
    void adicionarPontosNovamente() {
        ClientePontosFidelidade pontos = new ClientePontosFidelidade(9);
        var pontosAtualizados = pontos.adicionarPontos(11);
        Assertions.assertThat(pontosAtualizados.valor()).isEqualTo(20);
    }

    @Test
    void adicionarPontosNegativos() {
        ClientePontosFidelidade pontos = new ClientePontosFidelidade(7);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pontos.adicionarPontos(-5));
        Assertions.assertThat(pontos.valor()).isEqualTo(7);
    }
}
