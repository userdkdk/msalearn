package com.example.catalogservice.service;

import com.example.catalogservice.dto.response.ResponseCatalog;
import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.domain.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public List<ResponseCatalog> getAllCatalogs() {
        List<Catalog> res = catalogRepository.findAll();

        return res.stream()
                .map(ResponseCatalog::of)
                .toList();
    }

    @Transactional
    public void decreaseStock(String productId, Integer qty) {
        Catalog catalog = catalogRepository.findByProductId(productId)
                .orElseThrow(()->new IllegalArgumentException());
        catalog.decreaseStock(qty);
    }
}
