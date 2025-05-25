package online.shops.simple.repositories.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import online.shops.simple.models.Product;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    Optional<Product> findByIdAndNotArchived(Long id);

    Product save(Product product);

    void deleteById(Long id);

    List<Product> findAll(int page, int size, String sortBy, boolean ascending);

    List<Product> search(
        String searchText,
        List<String> keywords,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        int page,
        int size,
        String sortBy,
        boolean ascending,
        Boolean isArchived
    );
    
    long countProducts(
        String searchText,
        List<String> keywords,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean isArchived
    );
}