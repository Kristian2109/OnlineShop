package online.shops.simple.dtos;

public record CreateImageDto(
    String mimeType,
    String base64Data
) {}