package online.shops.simple.repositories.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import online.shops.simple.models.Order;
import online.shops.simple.models.OrderStatus;

@Component
public class JpaOrderRepository implements OrderRepository {
    
    private final SpringDataOrderRepository springDataRepository;
    
    public JpaOrderRepository(SpringDataOrderRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }
    
    @Override
    public Optional<Order> findById(Long id) {
        return springDataRepository.findById(id);
    }
    
    @Override
    public List<Order> findByCustomerId(Long customerId, int page, int size, String sortBy, boolean ascending) {
        Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return springDataRepository.findByCustomerId(customerId, pageable)
                .getContent();
    }
    
    @Override
    public List<Order> findByStatus(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return springDataRepository.findByStatus(status, pageable)
                .getContent();
    }
    
    @Override
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return springDataRepository.findByOrderDateBetween(startDate, endDate, pageable)
                .getContent();
    }
    
    @Override
    public Order save(Order order) {
        return springDataRepository.save(order);
    }
    
    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
    
    @Override
    public List<Order> findAll(int page, int size, String sortBy, boolean ascending) {
        Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return springDataRepository.findAll(pageable)
                .getContent();
    }
} 