package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.PaginatedResult;
import org.lekitech.gafalag.dto.expression.*;
import org.lekitech.gafalag.entity.*;
import org.lekitech.gafalag.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final ExpressionMapper expressionMapper;

    public Expression getById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    public PaginatedResult<ExpressionResponse> getPaginated(int page, int size, Optional<String> languageId) {
        return getPaginated(page, size, languageId, "spelling", false);
    }

    @Transactional(readOnly = true)
    public PaginatedResult<ExpressionResponse> getPaginated(int page,
                                                            int size,
                                                            Optional<String> languageId,
                                                            String sortBy,
                                                            boolean descending) {
        var sort = Sort.by(sortBy);
        var pages = PageRequest.of(page, size, descending ? sort.descending() : sort.ascending());
        return expressionMapper.toDto(languageId.isPresent()
                ? repository.findAllByLanguageId(languageId.get(), pages)
                : repository.findAll(pages)
        );
    }

    public PaginatedResult<ExpressionResponse> getPaginated(int page, int size, Optional<String> languageId, String sortBy) {
        return getPaginated(page, size, languageId, sortBy, false);
    }

    public Expression save(ExpressionRequest request) {
        return repository.save(createExpression(
                request.expressionLanguageId(),
                request.definitionLanguageId(),
                sourceService.getById(request.sourceId()),
                request.spelling(),
                request.inflection(),
                request.definitions()
        ));
    }

    private Expression createExpression(String expressionLanguageId,
                                        String definitionLanguageId,
                                        Source source,
                                        String spelling,
                                        Optional<String> inflection,
                                        List<String> definitions) {
        val expressionLanguage = languageService.getById(expressionLanguageId);
        val definitionLanguage = languageService.getById(definitionLanguageId);
        val defs = definitions.stream().map(text -> new Definition(
                text, definitionLanguage, source
        )).toList();
        return new Expression(spelling, inflection.orElse(null), expressionLanguage, defs);
    }

    public void saveBatch(ExpressionBatchRequest request) {
        val expLang = request.expressionLanguageId();
        val defLang = request.definitionLanguageId();
        val source = sourceService.getOrCreate(request.name(), request.url().orElse(null));
        repository.saveAll(request.dictionary()
                .stream().map(article -> createExpression(
                        expLang, defLang, source,
                        article.spelling(),
                        article.inflection(),
                        article.definitions()
                )).toList()
        );
    }
}
