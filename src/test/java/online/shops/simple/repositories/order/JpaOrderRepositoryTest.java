package online.shops.simple.repositories.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import online.shops.simple.models.Account;
import online.shops.simple.models.Address;
import online.shops.simple.models.Order;
import online.shops.simple.models.OrderStatus;

@ExtendWith(MockitoExtension.class)
public class JpaOrderRepositoryTest {
    
    @Mock
    private SpringDataOrderRepository springDataRepository;
    
    @InjectMocks
    private JpaOrderRepository orderRepository;
    
    private Order order1;
    private Order order2;
    
    @BeforeEach
    public void setup() {
        Account account = new Account();
        account.setId(1L);
        account.setUsername("testuser");
        
        order1 = new Order();
        order1.setId(1L);
        order1.setCustomer(account);
        order1.setStatus(OrderStatus.PENDING);
        order1.setOrderDate(LocalDateTime.now().minusDays(1));
        order1.setTotalPrice(new BigDecimal("99.99"));
        
        Address address1 = new Address("123 Main St", "Boston", "MA", "02108", "USA");
        order1.setShippingAddress(address1);
        
        order2 = new Order();
        order2.setId(2L);
        order2.setCustomer(account);
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setOrderDate(LocalDateTime.now());
        order2.setTotalPrice(new BigDecimal("149.99"));
        
        Address address2 = new Address("456 Oak St", "Boston", "MA", "02108", "USA");
        order2.setShippingAddress(address2);
    }
    
    @Test
    public void testFindById() {
        // Arrange
        when(springDataRepository.findById(1L)).thenReturn(Optional.of(order1));
        
        // Act
        Optional<Order> result = orderRepository.findById(1L);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getStatus()).isEqualTo(OrderStatus.PENDING);
    }
    
    @Test
    public void testFindByCustomerId() {
        // Arrange
        Page<Order> page = new PageImpl<>(Arrays.asList(order1, order2));
        when(springDataRepository.findByCustomerId(eq(1L), any(Pageable.class))).thenReturn(page);
        
        // Act
        List<Order> results = orderRepository.findByCustomerId(1L, 0, 10, "orderDate", true);
        
        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).contains(order1, order2);
    }
    
    @Test
    public void testFindByStatus() {
        // Arrange
        Page<Order> page = new PageImpl<>(Arrays.asList(order1));
        when(springDataRepository.findByStatus(eq(OrderStatus.PENDING), any(Pageable.class))).thenReturn(page);
        
        // Act
        List<Order> results = orderRepository.findByStatus(OrderStatus.PENDING, 0, 10);
        
        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }
    
    @Test
    public void testFindByDateRange() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        
        Page<Order> page = new PageImpl<>(Arrays.asList(order1, order2));
        when(springDataRepository.findByOrderDateBetween(eq(start), eq(end), any(Pageable.class))).thenReturn(page);
        
        // Act
        List<Order> results = orderRepository.findByDateRange(start, end, 0, 10);
        
        // Assert
        assertThat(results).hasSize(2);
    }
    
    @Test
    public void testSave() {
        // Arrange
        when(springDataRepository.save(order1)).thenReturn(order1);
        
        // Act
        Order savedOrder = orderRepository.save(order1);
        
        // Assert
        assertThat(savedOrder).isSameAs(order1);
    }
    
    @Test
    public void testFindAll() {
        // Arrange
        Page<Order> page = new PageImpl<>(Arrays.asList(order1, order2));
        when(springDataRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        // Act
        List<Order> results = orderRepository.findAll(0, 10, "id", false);
        
        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).containsExactly(order1, order2);
    }
} 