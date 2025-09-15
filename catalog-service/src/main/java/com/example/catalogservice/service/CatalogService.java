package com.example.catalogservice.service;

import com.example.catalogservice.api.ResponseCatalog;
import com.example.catalogservice.dto.CatalogMapper;
import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
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
    private final CatalogMapper catalogMapper;
    private final Environment env;

    public List<ResponseCatalog> getAllCatalogs() {
//        List<CatalogEntity> res =
//                Optional.ofNullable(catalogRepository.findAll())
//                        .orElse(Collections.emptyList());
        List<CatalogEntity> res = catalogRepository.findAll();
        return catalogMapper.toResponseList(res);
    }
}
