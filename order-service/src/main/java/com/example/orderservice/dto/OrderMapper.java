package com.example.orderservice.dto;

import com.example.orderservice.api.RequestOrder;
import com.example.orderservice.api.ResponseOrder;
import com.example.orderservice.jpa.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderMapper {

    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    OrderDto fromRequest(RequestOrder requestOrder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OrderEntity toEntity(OrderDto orderDto);

    ResponseOrder toResponse(OrderEntity orderEntity);

    List<ResponseOrder> toResponseList(Iterable<OrderEntity> orderEntity);
}
