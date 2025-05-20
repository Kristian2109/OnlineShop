package online.shops.simple.dtos;

import java.math.BigDecimal;

public record OrderItemDto(
    Long id,
    Long orderId,
    Long productId,
    String productName,
    String productDescription,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {} 