package online.shops.simple.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ExistingProductDto(
    Long id,
    String name,
    String description,
    String keywords,
    BigDecimal price,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<String> imagesBase64
) {}
