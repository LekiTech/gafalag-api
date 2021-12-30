package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.ExpressionBatchRequest;
import org.lekitech.gafalag.entity.Definition;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionService {

    private final ExpressionRepository repository;

    private final SourceService sourceService;
    private final LanguageService languageService;


    public Expression findById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    public Expression save(Expression expression) {
        return repository.save(expression);
    }

    public List<Expression> saveBatch(ExpressionBatchRequest expressionBatchRequest) {
        var sourceData = expressionBatchRequest.source;
        var expressionLanguage = languageService.getByIso3(sourceData.expressionLanguageIso3());
        var definitionLanguage = languageService.getByIso3(sourceData.definitionLanguageIso3());

        var source = sourceService.getOrCreate(sourceData.name(), sourceData.url());

        var expressions = expressionBatchRequest.dictionary.stream()
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
