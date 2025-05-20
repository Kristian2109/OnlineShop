package online.shops.simple.repositories.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import online.shops.simple.models.Order;
import online.shops.simple.models.OrderStatus;

public interface OrderRepository {
    
    Optional<Order> findById(Long id);
    
    List<Order> findByCustomerId(Long customerId, int page, int size, String sortBy, boolean ascending);
    
    List<Order> findByStatus(OrderStatus status, int page, int size);
    
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size);
    
    Order save(Order order);
    
    void deleteById(Long id);
    
    List<Order> findAll(int page, int size, String sortBy, boolean ascending);
} 