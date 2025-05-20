package online.shops.simple.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import online.shops.simple.dtos.CreateOrderDto;
import online.shops.simple.dtos.OrderDto;
import online.shops.simple.mappers.OrderMapper;
import online.shops.simple.models.Order;
import online.shops.simple.models.OrderStatus;
import online.shops.simple.repositories.account.AccountRepository;
import online.shops.simple.repositories.order.OrderRepository;
import online.shops.simple.repositories.product.ProductRepository;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    
    public OrderService(OrderRepository orderRepository, 
                        AccountRepository accountRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
    }
    
    @Transactional
    public OrderDto createOrder(CreateOrderDto createOrderDto) {
        Order order = OrderMapper.fromCreateDto(createOrderDto, accountRepository, productRepository);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toOrderDto(savedOrder);
    }
    
    public Optional<OrderDto> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper::toOrderDto);
    }
    
    public List<OrderDto> getOrdersByCustomerId(Long customerId, int page, int size, String sortBy, boolean ascending) {
        return orderRepository.findByCustomerId(customerId, page, size, sortBy, ascending)
                .stream()
                .map(OrderMapper::toOrderDto)
                .collect(Collectors.toList());
    }
    
    public List<OrderDto> getOrdersByStatus(OrderStatus status, int page, int size) {
        return orderRepository.findByStatus(status, page, size)
                .stream()
                .map(OrderMapper::toOrderDto)
                .collect(Collectors.toList());
    }
    
    public List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        return orderRepository.findByDateRange(startDate, endDate, page, size)
                .stream()
                .map(OrderMapper::toOrderDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public Optional<OrderDto> updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Order order = orderOpt.get();
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return Optional.of(OrderMapper.toOrderDto(updatedOrder));
    }
    
    @Transactional
    public boolean deleteOrder(Long orderId) {
        if (orderRepository.findById(orderId).isPresent()) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
    
    public List<OrderDto> getAllOrders(int page, int size, String sortBy, boolean ascending) {
        return orderRepository.findAll(page, size, sortBy, ascending)
                .stream()
                .map(OrderMapper::toOrderDto)
                .collect(Collectors.toList());
    }
} 