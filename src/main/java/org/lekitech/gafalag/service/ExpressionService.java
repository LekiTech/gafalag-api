package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.ExpressionBatchRequest;
import org.lekitech.gafalag.dto.ExpressionDto;
import org.lekitech.gafalag.dto.PaginatedResult;
import org.lekitech.gafalag.entity.Definition;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionService {

    private final ExpressionRepository repository;

    private final SourceService sourceService;
    private final LanguageService languageService;


    public Expression getById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    public PaginatedResult<ExpressionDto> getPaginated(int page, int size, Optional<String> languageIso3) {
        return getPaginated(page, size, languageIso3, "spelling", false);
    }

    public PaginatedResult<ExpressionDto> getPaginated(int page, int size, Optional<String> languageIso3, String sortBy) {
        return getPaginated(page, size, languageIso3, sortBy, false);
    }

    @Transactional(readOnly = true)
    public PaginatedResult<ExpressionDto> getPaginated(int page, int size, Optional<String> languageIso3, String sortBy, boolean descending) {
        var sort = Sort.by(sortBy);
        var pages = PageRequest.of(page, size, descending ? sort.descending() : sort.ascending());
        System.out.println("CALLED: getPaginated");
        Page<Expression> pageDb;
        if (languageIso3.isPresent()) {
            var expressionLanguage = languageService.getByIso3(languageIso3.get());
            pageDb = repository.findAllByLanguageId(expressionLanguage.id, pages);
        } else {
            pageDb = repository.findAll(pages);
        }
        var expressions = pageDb.stream().map(ExpressionDto::new).toList();
        return new PaginatedResult<>(pageDb.getTotalElements(), pageDb.getTotalPages(), pageDb.getNumber(), pageDb.getSize(), expressions);
    }

    public Expression save(Expression expression) {
        return repository.save(expression);
    }

    public List<Expression> saveBatch(ExpressionBatchRequest expressionBatchRequest) {
        var expressionLanguage = languageService.getByIso3(expressionBatchRequest.expressionLanguageIso3());
        var definitionLanguage = languageService.getByIso3(expressionBatchRequest.definitionLanguageIso3());

        var source = sourceService.getOrCreate(expressionBatchRequest.name(), expressionBatchRequest.url());

        var expressions = expressionBatchRequest.dictionary().stream()
                .map(article -> {
                    var definitions = article.definitions().stream()
                            .map(definition -> new Definition(definition, definitionLanguage, source))
                            .toList();
                    return new Expression(article.spelling(), article.inflection(), expressionLanguage, definitions);
                })
                .toList();

        return repository.saveAll(expressions);
    }
}
