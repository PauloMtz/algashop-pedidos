package com.algashop.domain.repository;

import com.algashop.domain.models.AggregateRoot;

public interface RemoverRepositorio<T extends AggregateRoot<ID>, ID> extends RepositorioApp<T, ID> {
    void remover(T t);
    void remover(ID id);
}
