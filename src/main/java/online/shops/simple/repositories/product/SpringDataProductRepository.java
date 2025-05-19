package online.shops.simple.repositories.product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import online.shops.simple.models.Product;

public interface SpringDataProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN p.keywords k
        WHERE (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) 
                             OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:keywords IS NULL OR k.name IN :keywords)
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    """)
    Page<Product> searchProducts(
        @Param("search") String search,
        @Param("keywords") List<String> keywords,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        Pageable pageable
    );
}
