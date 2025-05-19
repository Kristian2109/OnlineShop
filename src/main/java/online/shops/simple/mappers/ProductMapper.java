package online.shops.simple.mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import online.shops.simple.dtos.CreateImageDto;
import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.models.Keyword;
import online.shops.simple.models.Product;
import online.shops.simple.models.ProductImage;
import online.shops.simple.repositories.keyword.KeywordRepository;

public class ProductMapper {

    private static KeywordRepository keywordRepository;

    public static void setKeywordRepository(KeywordRepository repository) {
        keywordRepository = repository;
    }

    public static ExistingProductDto toExistingDto(Product product) {
        List<String> encodedImages = product.getImages().stream()
            .map(ProductMapper::encodeImage)
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
            encodedImages
        );
    }

    public static Product fromCreateDto(CreateProductDto dto, KeywordRepository keywordRepo) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

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
            List<ProductImage> imageList = dto.images().stream()
                .map(ProductMapper::mapImage)
                .collect(Collectors.toList());

            imageList.forEach(img -> img.setProduct(product));
            product.setImages(imageList);
        }

        return product;
    }

    public static Product fromCreateDto(CreateProductDto dto) {
        if (keywordRepository == null) {
            throw new IllegalStateException("KeywordRepository must be set before calling this method");
        }
        return fromCreateDto(dto, keywordRepository);
    }

    private static ProductImage mapImage(CreateImageDto dto) {
        ProductImage image = new ProductImage();
        image.setMimeType(dto.mimeType());
        image.setImageData(Base64.getDecoder().decode(dto.base64Data()));
        return image;
    }

    private static String encodeImage(ProductImage image) {
        String base64 = Base64.getEncoder().encodeToString(image.getImageData());
        return "data:" + image.getMimeType() + ";base64," + base64;
    }
}
