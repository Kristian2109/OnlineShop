package online.shops.simple.dtos;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductDto(
    String name,
    String description,
    String keywords,
    BigDecimal price,
    List<CreateImageDto> images
) {}