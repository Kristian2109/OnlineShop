package online.shops.simple.dtos;

import java.util.List;

public record ProductPageDto(
    List<ExistingProductDto> products,
    long totalCount
) {} 