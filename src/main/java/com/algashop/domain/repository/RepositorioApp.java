package com.algashop.domain.repository;

import java.util.Optional;

import com.algashop.domain.models.AggregateRoot;

public interface RepositorioApp<T extends AggregateRoot<ID>, ID> {
    Optional<T> buscaId(ID id);
    boolean existente(ID id);
    void adicionar(T aggregateRoot);
    int contar();
}
