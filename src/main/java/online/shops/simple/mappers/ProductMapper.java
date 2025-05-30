package online.shops.simple.mappers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.dtos.ProductImageDto;
import online.shops.simple.models.Keyword;
import online.shops.simple.models.Product;
import online.shops.simple.models.ProductImage;
import online.shops.simple.repositories.keyword.KeywordRepository;
import online.shops.simple.services.ImageService;

public class ProductMapper {

    private static KeywordRepository keywordRepository;

    public static void setKeywordRepository(KeywordRepository repository) {
        keywordRepository = repository;
    }

    public static ExistingProductDto toExistingDto(Product product) {
        List<ProductImageDto> imageUrls = product.getImages().stream()
            .map(image -> new ProductImageDto(
                "/images/" + image.getImagePath(),
                image.getFilename(),
                image.getPosition()
            ))
            .collect(Collectors.toList());

        List<String> keywordNames = product.getKeywords().stream()
            .map(Keyword::getName)
            .collect(Collectors.toList());

        return new ExistingProductDto(
            product.getId(),
            product.getName(),
            product.getDescription(),
            keywordNames,
            product.getPrice(),
            product.getCreatedAt(),
            product.getUpdatedAt(),
            imageUrls,
            product.getIsArchived()
        );
    }

    public static Product fromCreateDto(CreateProductDto dto, KeywordRepository keywordRepo, ImageService imageService) {
        return fromCreateDto(dto, keywordRepo, imageService, null);
    }

    public static Product fromCreateDto(CreateProductDto dto, KeywordRepository keywordRepo, ImageService imageService, Boolean isArchived) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        if (isArchived != null) {
            product.setIsArchived(isArchived);
        }

        if (dto.keywords() != null && !dto.keywords().isEmpty()) {
            List<Keyword> existingKeywords = keywordRepo.findAllByNameIn(dto.keywords());
            
            var existingKeywordMap = existingKeywords.stream()
                .collect(Collectors.toMap(Keyword::getName, k -> k));
            
            List<Keyword> productKeywords = new ArrayList<>();
            
            for (String keywordName : dto.keywords()) {
                if (existingKeywordMap.containsKey(keywordName)) {
                    productKeywords.add(existingKeywordMap.get(keywordName));
                } else {
                    Keyword newKeyword = new Keyword();
                    newKeyword.setName(keywordName);
                    productKeywords.add(newKeyword);
                }
            }
            product.setKeywords(productKeywords);
        }

        if (dto.images() != null && !dto.images().isEmpty()) {
            List<ProductImage> imageList = new ArrayList<>();
            for (int i = 0; i < dto.images().size(); i++) {
                MultipartFile file = dto.images().get(i);
                if (file != null && !file.isEmpty()) {
                    try {
                        ProductImage productImage = imageService.storeImage(file, i);
                        productImage.setProduct(product);
                        imageList.add(productImage);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store image: " + file.getOriginalFilename(), e);
                    }
                }
            }
            product.setImages(imageList);
        }

        return product;
    }

    public static Product fromCreateDto(CreateProductDto dto) {
        if (keywordRepository == null) {
            throw new IllegalStateException("KeywordRepository must be set before calling this method.");
        }
        throw new IllegalStateException("This fromCreateDto method is incomplete without ImageService. Use the overloaded version.");
    }
}
