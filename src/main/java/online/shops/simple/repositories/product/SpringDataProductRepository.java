package online.shops.simple.repositories.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import online.shops.simple.models.Product;

public interface SpringDataProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isArchived = false")
    Optional<Product> findByIdAndNotArchived(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN p.keywords k
        WHERE p.isArchived = false
        AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) 
                             OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:keywords IS NULL OR k.name IN :keywords)
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (:isArchived IS NULL OR p.isArchived = :isArchived)
    """)
    Page<Product> searchProducts(
        @Param("search") String search,
        @Param("keywords") List<String> keywords,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        Pageable pageable,
        @Param("isArchived") Boolean isArchived
    );
    
    @Query("""
        SELECT COUNT(DISTINCT p) FROM Product p
        LEFT JOIN p.keywords k
        WHERE p.isArchived = false
        AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) 
                             OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:keywords IS NULL OR k.name IN :keywords)
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (:isArchived IS NULL OR p.isArchived = :isArchived)
    """)
    long countProducts(
        @Param("search") String search,
        @Param("keywords") List<String> keywords,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("isArchived") Boolean isArchived
    );
}
