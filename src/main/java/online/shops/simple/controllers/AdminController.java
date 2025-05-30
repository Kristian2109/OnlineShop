package online.shops.simple.controllers;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import online.shops.simple.dtos.UpdateProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.services.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminController {
    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ExistingProductDto> createProduct(
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam BigDecimal price,
        @RequestParam(required = false) List<String> keywords,
        @RequestParam(name = "images", required = false) List<MultipartFile> images,
        @RequestParam(defaultValue = "false") Boolean isArchived
    ) {
        CreateProductDto createDto = new CreateProductDto(name, description, keywords, price, images);
        ExistingProductDto product = productService.createProduct(createDto, isArchived);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping(value = "/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<ExistingProductDto> updateProduct(
        @PathVariable Long productId,
        @Valid UpdateProductDto updateProductDto
    ) {
        return productService.updateProduct(productId, updateProductDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
