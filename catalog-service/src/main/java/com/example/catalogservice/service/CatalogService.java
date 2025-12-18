package com.example.catalogservice.service;

import com.example.catalogservice.dto.response.ResponseCatalog;
import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.domain.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public List<ResponseCatalog> getAllCatalogs() {
        List<Catalog> res = catalogRepository.findAll();

        return res.stream()
                .map(ResponseCatalog::of)
                .toList();
    }
}
