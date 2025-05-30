package online.shops.simple.dtos;

public record CreateOrderItemDto(
    Long productId,
    Integer quantity
) {} 