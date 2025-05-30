package online.shops.simple.mappers;

import java.util.stream.Collectors;

import online.shops.simple.dtos.CreateOrderDto;
import online.shops.simple.dtos.OrderDto;
import online.shops.simple.dtos.OrderItemDto;
import online.shops.simple.models.Account;
import online.shops.simple.models.Order;
import online.shops.simple.models.OrderItem;
import online.shops.simple.models.OrderStatus;
import online.shops.simple.models.Product;
import online.shops.simple.repositories.account.AccountRepository;
import online.shops.simple.repositories.product.ProductRepository;

public class OrderMapper {
    
    public static Order fromCreateDto(CreateOrderDto dto, AccountRepository accountRepo, ProductRepository productRepo) {
        Order order = new Order();
        
        if (dto.customerId() != null) {
            Account customer = accountRepo.findById(dto.customerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            order.setCustomer(customer);
        }
        
        order.setShippingAddress(dto.shippingAddress());
        order.setPaymentMethod(dto.paymentMethod());
        order.setNotes(dto.notes());
        order.setStatus(OrderStatus.PENDING);
        order.setPhoneNumber(dto.phoneNumber());
        order.setEmail(dto.email());

        dto.items().forEach(itemDto -> {
            Product product = productRepo.findById(itemDto.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            
            OrderItem orderItem = new OrderItem(product, itemDto.quantity());
            order.addItem(orderItem);
        });
        
        order.calculateTotalPrice();
        
        return order;
    }
    
    public static OrderDto toOrderDto(Order order) {
        Long customerId = null;
        String customerUsername = null;
        
        if (order.getCustomer() != null) {
            customerId = order.getCustomer().getId();
            customerUsername = order.getCustomer().getUsername();
        }
        
        return new OrderDto(
                order.getId(),
                customerId,
                customerUsername,
                order.getItems().stream()
                        .map(OrderMapper::toOrderItemDto)
                        .collect(Collectors.toList()),
                order.getOrderDate(),
                order.getEmail(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getShippingAddress(),
                order.getPaymentMethod(),
                order.getTrackingNumber(),
                order.getNotes(),
                order.getPhoneNumber(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
    
    public static OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getDescription(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getSubtotal()
        );
    }
} 