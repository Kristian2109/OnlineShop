package online.shops.simple.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import online.shops.simple.mappers.ProductMapper;
import online.shops.simple.repositories.keyword.KeywordRepository;

@Configuration
public class MapperConfig {

    private final KeywordRepository keywordRepository;

    public MapperConfig(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @PostConstruct
    public void init() {
        ProductMapper.setKeywordRepository(keywordRepository);
    }
} 