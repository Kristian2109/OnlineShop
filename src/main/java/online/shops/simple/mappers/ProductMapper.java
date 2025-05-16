package online.shops.simple.mappers;

import online.shops.simple.dtos.CreateImageDto;
import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.models.Product;
import online.shops.simple.models.ProductImage;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ExistingProductDto toExistingDto(Product product) {
        List<String> encodedImages = product.getImages().stream()
            .map(ProductMapper::encodeImage)
            .collect(Collectors.toList());

        return new ExistingProductDto(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getKeywords(),
            product.getPrice(),
            product.getCreatedAt(),
            product.getUpdatedAt(),
            encodedImages
        );
    }

    public static Product fromCreateDto(CreateProductDto dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setKeywords(dto.keywords());
        product.setPrice(dto.price());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        if (dto.images() != null && !dto.images().isEmpty()) {
            List<ProductImage> imageList = dto.images().stream()
                .map(ProductMapper::mapImage)
                .collect(Collectors.toList());

            imageList.forEach(img -> img.setProduct(product)); // maintain bidirectional link
            product.setImages(imageList);
        }

        return product;
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
