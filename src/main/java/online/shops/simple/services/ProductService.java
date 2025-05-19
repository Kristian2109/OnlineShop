package online.shops.simple.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.models.Product;
import online.shops.simple.repositories.product.ProductRepository;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<ExistingProductDto> getProducts(
            int page,
            int limit,
            String sortBy,
            String sortOrder,
            BigDecimal priceMin,
            BigDecimal priceMax,
            String search,
            List<String> keywords) {
        
        boolean ascending = sortOrder.equalsIgnoreCase("asc");
        
        List<Product> products = productRepository.search(
            search,
            keywords,
            priceMin,
            priceMax,
            page,
            limit,
            sortBy,
            ascending
        );
        
        return products.stream()
            .map(ProductMapper::toExistingDto)
            .collect(Collectors.toList());
    }
    
    public Optional<ExistingProductDto> getProductById(Long productId) {
        return productRepository.findById(productId)
            .map(ProductMapper::toExistingDto);
    }
} 