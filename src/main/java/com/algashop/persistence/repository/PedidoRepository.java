package com.algashop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algashop.persistence.entity.PedidoPersistenceEntity;

public interface PedidoRepository extends JpaRepository<PedidoPersistenceEntity, Long> {
}
