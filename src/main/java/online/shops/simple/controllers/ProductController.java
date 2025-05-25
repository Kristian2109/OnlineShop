package online.shops.simple.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.dtos.ProductPageDto;
import online.shops.simple.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductPageDto getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int limit,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortOrder,
        @RequestParam(required = false) BigDecimal priceMin,
        @RequestParam(required = false) BigDecimal priceMax,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) List<String> keywords,
        @RequestParam(required = false) Boolean isArchived
    ) {
        return productService.getProducts(
            page, limit, sortBy, sortOrder, priceMin, priceMax, search, keywords, isArchived
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ExistingProductDto> getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
