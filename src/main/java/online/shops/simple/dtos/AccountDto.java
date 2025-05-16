package online.shops.simple.dtos;

import java.time.LocalDateTime;

public record AccountDto(Long id, String username, LocalDateTime createdAt) {}
