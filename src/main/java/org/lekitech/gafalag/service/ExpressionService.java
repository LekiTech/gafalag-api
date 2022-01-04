package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.entity.Definition;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public PaginatedResult<ExpressionDto> getPaginated(int page, int size, Optional<String> languageIso3, String sortBy) {
        return getPaginated(page, size, languageIso3, sortBy, false);
    }

    public Expression save(ExpressionRequest request) {
        val source = sourceService.getById(request.sourceId());
        val expressionLanguage = languageService.getByIso3(request.expressionLanguageIso3());
        val definitionLanguage = languageService.getByIso3(request.definitionLanguageIso3());
        return repository.save(
                new Expression(
                        request.spelling(),
                        request.inflection(),
                        expressionLanguage,
                        request.definitions().stream()
                                .map(value -> new Definition(value, definitionLanguage, source))
                                .toList()
                )
        );
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
