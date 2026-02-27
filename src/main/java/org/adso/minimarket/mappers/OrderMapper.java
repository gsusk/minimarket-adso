package org.adso.minimarket.mappers;


import org.adso.minimarket.dto.OrderSummary;
import org.adso.minimarket.models.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "userId", expression = "java(order.getUser().getId())")
    OrderSummary toOrderSummaryDto(Order order);
}
