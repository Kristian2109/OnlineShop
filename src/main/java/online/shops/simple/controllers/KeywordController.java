package online.shops.simple.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import online.shops.simple.services.KeywordService;

@RestController
@RequestMapping("/api/keywords")
public class KeywordController {

    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping
    public ResponseEntity<List<KeywordDto>> getAllKeywords() {
        return ResponseEntity.ok(
            keywordService.getAllKeywords().stream()
                .map(keyword -> new KeywordDto(keyword.getId(), keyword.getName()))
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{name}")
    public ResponseEntity<KeywordDto> getKeywordByName(@PathVariable String name) {
        return keywordService.getKeywordByName(name)
            .map(keyword -> new KeywordDto(keyword.getId(), keyword.getName()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<KeywordDto>> getKeywordsByNames(@RequestParam List<String> names) {
        if (names == null || names.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(
            keywordService.getKeywordsByNames(names).stream()
                .map(keyword -> new KeywordDto(keyword.getId(), keyword.getName()))
                .collect(Collectors.toList())
        );
    }

    public static class KeywordDto {
        private final Long id;
        private final String name;

        public KeywordDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
} 