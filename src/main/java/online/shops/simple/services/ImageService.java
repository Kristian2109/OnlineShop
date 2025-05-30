package online.shops.simple.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import online.shops.simple.models.ProductImage;

@Service
public class ImageService {

    private final Path imageStorageLocation = Paths.get("product-images").toAbsolutePath().normalize();

    public ProductImage storeImage(MultipartFile file, int position) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i);
        }
        String storedFilename = UUID.randomUUID().toString() + extension;

        Path targetLocation = this.imageStorageLocation.resolve(storedFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        ProductImage productImage = new ProductImage();
        productImage.setImagePath(storedFilename);
        productImage.setFilename(originalFilename);
        productImage.setPosition(position);

        return productImage;
    }

    public void deleteImage(String imagePath) throws IOException {
        if (imagePath == null || imagePath.isBlank()) {
            return; 
        }
        Path targetLocation = this.imageStorageLocation.resolve(imagePath).normalize();
        Files.deleteIfExists(targetLocation);
    }
} 