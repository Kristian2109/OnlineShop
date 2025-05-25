package online.shops.simple.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.services.AdminService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ExistingProductDto> createProduct(
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam BigDecimal price,
        @RequestParam(required = false) List<String> keywords,
        @RequestParam("images") List<MultipartFile> images,
        @RequestParam(defaultValue = "false") Boolean isArchived
    ) {
        ExistingProductDto product = adminService.createProduct(name, description, price, keywords, images, isArchived);
        return ResponseEntity.ok(product);
    }

    @PutMapping(value = "/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<ExistingProductDto> updateProduct(
        @PathVariable Long productId,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam BigDecimal price,
        @RequestParam(required = false) List<String> keywords,
        @RequestParam("images") List<MultipartFile> images,
        @RequestParam(defaultValue = "false") Boolean isArchived
    ) {
        return adminService.updateProduct(productId, name, description, price, keywords, images, isArchived)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean deleted = adminService.deleteProduct(productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
