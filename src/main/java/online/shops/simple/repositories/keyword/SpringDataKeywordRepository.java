package online.shops.simple.repositories.keyword;

import online.shops.simple.models.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataKeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);
    List<Keyword> findAllByNameIn(List<String> names);
} 