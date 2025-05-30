package online.shops.simple.repositories.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import online.shops.simple.models.ProductImage;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Long> {
}
