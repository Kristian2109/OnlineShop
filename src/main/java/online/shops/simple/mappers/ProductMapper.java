package online.shops.simple.mappers;

import online.shops.simple.dtos.ProductDto;
import online.shops.simple.models.Product;
import online.shops.simple.models.ProductImage;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        List<String> encodedImages = product.getImages().stream()
            .map(ProductMapper::encodeImage)
            .collect(Collectors.toList());

        return new ProductDto(
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

    private static String encodeImage(ProductImage image) {
        String base64 = Base64.getEncoder().encodeToString(image.getImageData());
        return "data:" + image.getMimeType() + ";base64," + base64;
    }
}
