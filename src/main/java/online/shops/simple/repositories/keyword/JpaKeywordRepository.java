package online.shops.simple.repositories.keyword;

import online.shops.simple.models.Keyword;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaKeywordRepository implements KeywordRepository {
    private final SpringDataKeywordRepository springRepo;

    public JpaKeywordRepository(SpringDataKeywordRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Keyword> findByName(String name) {
        return springRepo.findByName(name);
    }

    @Override
    public Keyword save(Keyword keyword) {
        return springRepo.save(keyword);
    }

    @Override
    public List<Keyword> findAllByNameIn(List<String> names) {
        return springRepo.findAllByNameIn(names);
    }

    @Override
    public List<Keyword> findAll() {
        return springRepo.findAll();
    }
} 