package online.shops.simple.controllers;

import online.shops.simple.dtos.CreateImageDto;
import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.models.Product;
import online.shops.simple.repositories.keyword.KeywordRepository;
import online.shops.simple.repositories.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/products";
    }

    @BeforeEach
    public void setup() {
        List<Product> allProducts = productRepository.findAll(0, 100, "id", true);
        for (Product product : allProducts) {
            productRepository.deleteById(product.getId());
        }

        createTestProducts();
    }

    private void createTestProducts() {
        List<String> keywords1 = Arrays.asList("electronic", "gadget");
        CreateProductDto dto1 = new CreateProductDto(
                "Smartphone",
                "Latest model smartphone with advanced features",
                keywords1,
                new BigDecimal("599.99"),
                new ArrayList<>() // No images for test simplicity
        );
        Product product1 = ProductMapper.fromCreateDto(dto1, keywordRepository);
        productRepository.save(product1);

        List<String> keywords2 = Arrays.asList("clothing", "fashion");
        CreateProductDto dto2 = new CreateProductDto(
                "T-shirt",
                "Cotton t-shirt in various colors",
                keywords2,
                new BigDecimal("19.99"),
                new ArrayList<>()
        );
        Product product2 = ProductMapper.fromCreateDto(dto2, keywordRepository);
        productRepository.save(product2);

        List<String> keywords3 = Arrays.asList("electronic", "audio");
        CreateProductDto dto3 = new CreateProductDto(
                "Headphones",
                "Wireless noise-cancelling headphones",
                keywords3,
                new BigDecimal("149.99"),
                new ArrayList<>()
        );
        Product product3 = ProductMapper.fromCreateDto(dto3, keywordRepository);
        productRepository.save(product3);
    }

    @Test
    public void testGetAllProducts() {
        ResponseEntity<List<ExistingProductDto>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
    }

    @Test
    public void testGetProductWithPriceFilter() {
        String url = getBaseUrl() + "?priceMin=100&priceMax=600";
        
        ResponseEntity<List<ExistingProductDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().stream()
                .allMatch(p -> p.price().compareTo(new BigDecimal("100")) >= 0 
                        && p.price().compareTo(new BigDecimal("600")) <= 0))
                .isTrue();
    }

    @Test
    public void testGetProductWithKeywordFilter() {
        String url = getBaseUrl() + "?keywords=electronic";
        
        ResponseEntity<List<ExistingProductDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().stream()
                .anyMatch(p -> p.name().equals("Smartphone")))
                .isTrue();
        assertThat(response.getBody().stream()
                .anyMatch(p -> p.name().equals("Headphones")))
                .isTrue();
    }

    @Test
    public void testGetProductWithSearchFilter() {
        String url = getBaseUrl() + "?search=head";
        
        ResponseEntity<List<ExistingProductDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).name()).isEqualTo("Headphones");
    }

    @Test
    public void testGetProductWithSorting() {
        String url = getBaseUrl() + "?sortBy=price&sortOrder=asc";
        
        ResponseEntity<List<ExistingProductDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
        
        BigDecimal previousPrice = BigDecimal.ZERO;
        for (ExistingProductDto product : response.getBody()) {
            assertThat(product.price().compareTo(previousPrice)).isGreaterThanOrEqualTo(0);
            previousPrice = product.price();
        }
    }

    @Test
    public void testGetProductWithPagination() {
        String url = getBaseUrl() + "?page=0&limit=2";
        
        ResponseEntity<List<ExistingProductDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        
        url = getBaseUrl() + "?page=1&limit=2";
        
        response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    public void testGetProductById() {
        ResponseEntity<List<ExistingProductDto>> allProductsResponse = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExistingProductDto>>() {}
        );
        
        assertThat(allProductsResponse.getBody()).isNotNull();
        assertThat(allProductsResponse.getBody().size()).isGreaterThan(0);
        
        Long productId = allProductsResponse.getBody().get(0).id();
        
        ResponseEntity<ExistingProductDto> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + productId,
                ExistingProductDto.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(productId);
    }

    @Test
    public void testGetProductByIdNotFound() {
        Long nonExistentId = 9999L;
        
        ResponseEntity<ExistingProductDto> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + nonExistentId,
                ExistingProductDto.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
} 