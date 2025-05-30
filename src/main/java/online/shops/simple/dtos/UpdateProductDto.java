package online.shops.simple.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Builder.Default
    private List<String> keywords = new ArrayList<>();

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Builder.Default
    private List<MultipartFile> newImages = new ArrayList<>();

    @Builder.Default
    private List<String> imagesToDelete = new ArrayList<>();

    @Builder.Default
    private Boolean isArchived = false;
}
