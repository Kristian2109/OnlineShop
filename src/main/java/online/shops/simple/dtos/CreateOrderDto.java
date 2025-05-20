package online.shops.simple.dtos;

import java.util.List;

import online.shops.simple.models.Address;

public record CreateOrderDto(
    Long customerId,
    List<CreateOrderItemDto> items,
    Address shippingAddress,
    String paymentMethod,
    String notes
) {} 