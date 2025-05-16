package online.shops.simple.controllers;

import online.shops.simple.dtos.CreateImageDto;
import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.models.Product;
import online.shops.simple.repositories.product.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<ExistingProductDto> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int limit,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortOrder,
        @RequestParam(required = false) BigDecimal priceMin,
        @RequestParam(required = false) BigDecimal priceMax,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) List<String> keywords
    ) {
        boolean ascending = sortOrder.equalsIgnoreCase("asc");
        String keywordString = (keywords == null || keywords.isEmpty()) ? null : String.join(",", keywords);

        List<Product> products = productRepository.search(
            search,
            keywordString,
            priceMin,
            priceMax,
            page,
            limit,
            sortBy,
            ascending
        );

        return products.stream()
            .map(ProductMapper::toExistingDto)
            .toList();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ExistingProductDto> getProductById(@PathVariable Long productId) {
        return productRepository.findById(productId)
            .map(ProductMapper::toExistingDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
