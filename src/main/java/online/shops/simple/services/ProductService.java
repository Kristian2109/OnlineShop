package online.shops.simple.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import online.shops.simple.dtos.UpdateProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import online.shops.simple.dtos.CreateProductDto;
import online.shops.simple.dtos.ExistingProductDto;
import online.shops.simple.dtos.ProductPageDto;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.models.Keyword;
import online.shops.simple.models.Product;
import online.shops.simple.models.ProductImage;
import online.shops.simple.repositories.keyword.KeywordRepository;
import online.shops.simple.repositories.product.ProductRepository;

@Service
public class ProductService {
    
    private static final int MAX_PRODUCTS_LIMIT = 50;
    
    private final ProductRepository productRepository;
    private final KeywordRepository keywordRepository;
    private final ImageService imageService;
    
    public ProductService(ProductRepository productRepository, KeywordRepository keywordRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.keywordRepository = keywordRepository;
        this.imageService = imageService;
        ProductMapper.setKeywordRepository(keywordRepository);
    }
    
    public ProductPageDto getProducts(
            int page,
            int limit,
            String sortBy,
            String sortOrder,
            BigDecimal priceMin,
            BigDecimal priceMax,
            String search,
            List<String> keywords,
            Boolean isArchived
            ) {
        
        boolean ascending = sortOrder.equalsIgnoreCase("asc");
        
        int cappedLimit = Math.min(limit, MAX_PRODUCTS_LIMIT);
        
        List<Product> products = productRepository.search(
            search,
            keywords,
            priceMin,
            priceMax,
            page,
            cappedLimit,
            sortBy,
            ascending,
            isArchived
        );
        
        List<ExistingProductDto> productDtos = products.stream()
            .map(ProductMapper::toExistingDto)
            .collect(Collectors.toList());
        
        long totalCount = productRepository.countProducts(
            search,
            keywords,
            priceMin,
            priceMax,
            isArchived
        );
        
        return new ProductPageDto(productDtos, totalCount);
    }
    
    public Optional<ExistingProductDto> getProductById(Long productId) {
        return productRepository.findByIdAndNotArchived(productId)
            .map(ProductMapper::toExistingDto);
    }

    @Transactional
    public ExistingProductDto createProduct(CreateProductDto createProductDto) {
        return createProduct(createProductDto, false);
    }

    @Transactional
    public ExistingProductDto createProduct(CreateProductDto createProductDto, Boolean isArchived) {
        Product product = ProductMapper.fromCreateDto(createProductDto, keywordRepository, imageService, isArchived);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toExistingDto(savedProduct);
    }

    @Transactional
    public Optional<ExistingProductDto> updateProduct(Long productId, UpdateProductDto updateDto) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            return Optional.empty();
        }

        Product existingProduct = productOptional.get();

        existingProduct.setName(updateDto.getName());
        existingProduct.setDescription(updateDto.getDescription());
        existingProduct.setPrice(updateDto.getPrice());

        List<Keyword> keywords = new ArrayList<>();
        for (String keywordName : updateDto.getKeywords()) {
            Keyword keyword = keywordRepository.findByName(keywordName)
                .orElseGet(() -> {
                    Keyword newKeyword = new Keyword();
                    newKeyword.setName(keywordName);
                    return newKeyword;
                });
            keywords.add(keyword);
        }
        existingProduct.setKeywords(keywords);

        List<String> imagesToDelete = new ArrayList<>(updateDto.getImagesToDelete());
        for (String oldImage : imagesToDelete) {
            try {
                String filename = oldImage.replaceAll(".*/", "");
                imageService.deleteImage(filename);
            } catch (IOException e) {
                System.err.println("[ProductService] Failed to delete old image file: " + oldImage + " Error: " + e.getMessage());
            }
        }

        List<ProductImage> lastingImages = existingProduct.getImages()
            .stream()
            .filter(image -> !updateDto.getImagesToDelete().contains(image.getImagePath()))
            .toList();

        existingProduct.setImages(lastingImages);

        for (int i = 0; i < updateDto.getNewImages().size(); i++) {
            MultipartFile file = updateDto.getNewImages().get(i);
            if (file != null && !file.isEmpty()) {
                try {
                    ProductImage newProductImage = imageService.storeImage(file, i);
                    newProductImage.setProduct(existingProduct);
                    existingProduct.getImages().add(newProductImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to store new image: " + file.getOriginalFilename(), e);
                }
            }
        }
        
        Product savedProduct = productRepository.save(existingProduct);
        return Optional.of(ProductMapper.toExistingDto(savedProduct));
    }

    @Transactional
    public void deleteProduct(Long productId) throws IOException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            return;
        }

        Product product = productOptional.get();
        if (product.getImages() != null) {
            for (ProductImage image : product.getImages()) {
                imageService.deleteImage(image.getImagePath());
            }
        }
        productRepository.deleteById(productId);

    }
} 