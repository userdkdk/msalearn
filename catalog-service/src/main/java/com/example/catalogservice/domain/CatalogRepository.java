package com.example.catalogservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Integer> {
    Optional<Catalog> findByProductId(String productId);
}
