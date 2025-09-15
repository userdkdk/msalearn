package com.example.catalogservice.dto;

import com.example.catalogservice.api.ResponseCatalog;
import com.example.catalogservice.jpa.CatalogEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CatalogMapper {

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<ResponseCatalog> toResponseList(List<CatalogEntity> entity);
}
