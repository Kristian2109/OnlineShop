package online.shops.simple.controllers;

import online.shops.simple.dtos.CreateImageDto;
import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.models.Product;
import online.shops.simple.repositories.product.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/products")
public class AdminController {
    private final ProductRepository productRepository;

    public AdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ExistingProductDto> createProduct(
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam BigDecimal price,
        @RequestParam(required = false) String keywords,
        @RequestParam("images") List<MultipartFile> images
    ) {
        List<CreateImageDto> imageDtos = images.stream()
            .map(file -> {
                try {
                    return new CreateImageDto(
                        file.getContentType(),
                        Base64.getEncoder().encodeToString(file.getBytes())
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Error processing image", e);
                }
            })
            .collect(Collectors.toList());

        CreateProductDto createDto = new CreateProductDto(
            name, description, keywords, price, imageDtos
        );

        Product product = ProductMapper.fromCreateDto(createDto);
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(ProductMapper.toExistingDto(saved));
    }


    @PutMapping(value = "/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<ExistingProductDto> updateProduct(
        @PathVariable Long productId,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam BigDecimal price,
        @RequestParam(required = false) String keywords,
        @RequestParam("images") List<MultipartFile> images
    ) {
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Product existing = opt.get();

        List<CreateImageDto> imageDtos = images.stream()
            .map(file -> {
                try {
                    return new CreateImageDto(
                        file.getContentType(),
                        Base64.getEncoder().encodeToString(file.getBytes())
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Error processing image", e);
                }
            })
            .collect(Collectors.toList());

        CreateProductDto updateDto = new CreateProductDto(name, description, keywords, price, imageDtos);

        Product updated = ProductMapper.fromCreateDto(updateDto);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        productRepository.save(updated);

        return ResponseEntity.ok(ProductMapper.toExistingDto(updated));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        if (productRepository.findById(productId).isPresent()) {
            productRepository.deleteById(productId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
