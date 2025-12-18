package com.example.catalogservice.dto.response;

import com.example.catalogservice.domain.Catalog;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {
    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer stock;

    public static ResponseCatalog of(Catalog catalog) {
        return ResponseCatalog.builder()
                .productId(catalog.getProductId())
                .productName(catalog.getProductName())
                .unitPrice(catalog.getUnitPrice())
                .stock(catalog.getStock())
                .build();
    }
}
