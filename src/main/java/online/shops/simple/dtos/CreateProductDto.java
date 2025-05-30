package online.shops.simple.dtos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record CreateProductDto(
    String name,
    String description,
    List<String> keywords,
    BigDecimal price,
    List<MultipartFile> images
) {}