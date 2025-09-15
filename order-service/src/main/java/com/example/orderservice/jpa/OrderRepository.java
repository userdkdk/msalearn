package com.example.orderservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    Iterable<OrderEntity> findByUserId(String userId);
}
