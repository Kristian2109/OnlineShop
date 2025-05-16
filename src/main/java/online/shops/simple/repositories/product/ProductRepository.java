package online.shops.simple.repositories.product;


import online.shops.simple.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    List<Product> findAll(int page, int size, String sortBy, boolean ascending);

    List<Product> search(
        String searchText,
        String keywords,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        int page,
        int size,
        String sortBy,
        boolean ascending
    );
}