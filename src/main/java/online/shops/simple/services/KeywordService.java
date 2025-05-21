package online.shops.simple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import online.shops.simple.models.Keyword;
import online.shops.simple.repositories.keyword.KeywordRepository;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }

    public Optional<Keyword> getKeywordByName(String name) {
        return keywordRepository.findByName(name);
    }

    public List<Keyword> getKeywordsByNames(List<String> names) {
        return keywordRepository.findAllByNameIn(names);
    }
} 