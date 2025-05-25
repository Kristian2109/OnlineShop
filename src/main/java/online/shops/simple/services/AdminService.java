package online.shops.simple.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import online.shops.simple.dtos.CreateImageDto;
import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.models.Product;
import online.shops.simple.repositories.keyword.KeywordRepository;
import online.shops.simple.repositories.product.ProductRepository;

@Service
public class AdminService {
    private final ProductRepository productRepository;
    private final KeywordRepository keywordRepository;

    public AdminService(ProductRepository productRepository, KeywordRepository keywordRepository) {
        this.productRepository = productRepository;
        this.keywordRepository = keywordRepository;
    }

    public ExistingProductDto createProduct(
            String name,
            String description,
            BigDecimal price,
            List<String> keywords,
            List<MultipartFile> images,
            Boolean isArchived) {
        
        List<CreateImageDto> imageDtos = processImageFiles(images);
        List<String> keywordList = keywords != null ? keywords : new ArrayList<>();
        
        CreateProductDto createDto = new CreateProductDto(
            name, description, keywordList, price, imageDtos
        );

        Product product = ProductMapper.fromCreateDto(createDto, keywordRepository);
        Product saved = productRepository.save(product);
        return ProductMapper.toExistingDto(saved);
    }

    public Optional<ExistingProductDto> updateProduct(
            Long productId,
            String name,
            String description,
            BigDecimal price,
            List<String> keywords,
            List<MultipartFile> images,
            Boolean isArchived
            ) {
        
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isEmpty()) return Optional.empty();

        Product existing = opt.get();

        List<CreateImageDto> imageDtos = processImageFiles(images);
        List<String> keywordList = keywords != null ? keywords : new ArrayList<>();
        
        CreateProductDto updateDto = new CreateProductDto(name, description, keywordList, price, imageDtos);

        Product updated = ProductMapper.fromCreateDto(updateDto, keywordRepository);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setIsArchived(isArchived);
        Product saved = productRepository.save(updated);
        
        return Optional.of(ProductMapper.toExistingDto(saved));
    }

    public boolean deleteProduct(Long productId) {
        if (productRepository.findById(productId).isPresent()) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }
    
    private List<CreateImageDto> processImageFiles(List<MultipartFile> images) {
        return images.stream()
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
    }
} 