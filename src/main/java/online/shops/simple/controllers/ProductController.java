package online.shops.simple.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import online.shops.simple.dtos.CreateProductDto;
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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ExistingProductDto> createProduct(
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam(name = "keywords", required = false) List<String> keywords,
        @RequestParam("price") BigDecimal price,
        @RequestParam(name = "images", required = false) List<MultipartFile> images
    ) {
        CreateProductDto createProductDto = new CreateProductDto(name, description, keywords, price, images);
        ExistingProductDto createdProduct = productService.createProduct(createProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Log error e
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
