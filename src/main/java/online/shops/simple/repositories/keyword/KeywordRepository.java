package online.shops.simple.repositories.keyword;

import online.shops.simple.models.Keyword;
import java.util.Optional;
import java.util.List;

public interface KeywordRepository {
    Optional<Keyword> findByName(String name);
    Keyword save(Keyword keyword);
    List<Keyword> findAllByNameIn(List<String> names);
    List<Keyword> findAll();
} 