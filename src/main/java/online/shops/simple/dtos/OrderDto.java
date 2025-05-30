package online.shops.simple.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import online.shops.simple.models.Address;
import online.shops.simple.models.OrderStatus;

public record OrderDto(
    Long id,
    Long customerId,
    String customerUsername,
    List<OrderItemDto> items,
    LocalDateTime orderDate,
    String email,
    OrderStatus status,
    BigDecimal totalPrice,
    Address shippingAddress,
    String paymentMethod,
    String trackingNumber,
    String notes,
    String phoneNumber,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {} 