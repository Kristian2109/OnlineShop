package online.shops.simple.repositories.product;

import online.shops.simple.models.Product;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaProductRepository implements ProductRepository {

    private final SpringDataProductRepository springRepo;

    public JpaProductRepository(SpringDataProductRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return springRepo.findById(id);
    }

    @Override
    public Product save(Product product) {
        return springRepo.save(product);
    }

    @Override
    public void deleteById(Long id) {
        springRepo.deleteById(id);
    }

    @Override
    public List<Product> findAll(int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return springRepo.findAll(pageable).getContent();
    }

    @Override
    public List<Product> search(String searchText, String keywords, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return springRepo.searchProducts(searchText, keywords, minPrice, maxPrice, pageable).getContent();
    }
}