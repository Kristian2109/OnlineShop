package online.shops.simple.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.models.Product;
import online.shops.simple.repositories.keyword.KeywordRepository;
import online.shops.simple.repositories.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    public void setup() {
        // Create test products
        List<String> keywords1 = Arrays.asList("electronic", "gadget");
        CreateProductDto dto1 = new CreateProductDto(
                "Smartphone",
                "Latest model smartphone with advanced features",
                keywords1,
                new BigDecimal("599.99"),
                new ArrayList<>()
        );
        product1 = ProductMapper.fromCreateDto(dto1, keywordRepository);
        
        List<String> keywords2 = Arrays.asList("clothing", "fashion");
        CreateProductDto dto2 = new CreateProductDto(
                "T-shirt",
                "Cotton t-shirt in various colors",
                keywords2,
                new BigDecimal("19.99"),
                new ArrayList<>()
        );
        product2 = ProductMapper.fromCreateDto(dto2, keywordRepository);
        
        List<String> keywords3 = Arrays.asList("electronic", "audio");
        CreateProductDto dto3 = new CreateProductDto(
                "Headphones",
                "Wireless noise-cancelling headphones",
                keywords3,
                new BigDecimal("149.99"),
                new ArrayList<>()
        );
        product3 = ProductMapper.fromCreateDto(dto3, keywordRepository);
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        List<Product> productList = Arrays.asList(product1, product2, product3);
        when(productRepository.search(isNull(), isNull(), isNull(), isNull(), eq(0), eq(20), eq("createdAt"), eq(false)))
                .thenReturn(productList);
        
        // Act
        List<ExistingProductDto> result = productService.getProducts(0, 20, "createdAt", "desc", null, null, null, null);
        
        // Assert
        assertThat(result).hasSize(3);
        assertThat(result.stream().map(ExistingProductDto::name).toList())
                .containsExactly("Smartphone", "T-shirt", "Headphones");
    }

    @Test
    public void testGetProductsWithFilters() {
        // Arrange
        List<Product> filteredProducts = Arrays.asList(product1, product3);
        when(productRepository.search(
                isNull(), 
                eq(Arrays.asList("electronic")), 
                isNull(), 
                isNull(), 
                eq(0), 
                eq(20), 
                eq("createdAt"), 
                eq(false)))
                .thenReturn(filteredProducts);
        
        // Act
        List<ExistingProductDto> result = productService.getProducts(
                0, 20, "createdAt", "desc", null, null, null, Arrays.asList("electronic"));
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.stream().map(ExistingProductDto::name).toList())
                .containsExactly("Smartphone", "Headphones");
    }

    @Test
    public void testGetProductById() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));
        
        // Act
        Optional<ExistingProductDto> result = productService.getProductById(productId);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Smartphone");
        assertThat(result.get().price()).isEqualTo(new BigDecimal("599.99"));
    }

    @Test
    public void testGetProductByIdNotFound() {
        // Arrange
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // Act
        Optional<ExistingProductDto> result = productService.getProductById(productId);
        
        // Assert
        assertThat(result).isEmpty();
    }
} 