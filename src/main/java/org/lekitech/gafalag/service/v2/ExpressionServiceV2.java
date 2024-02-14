package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.*;
import org.lekitech.gafalag.dto.v2.mapper.DictionaryMapper;
import org.lekitech.gafalag.entity.v2.*;
import org.lekitech.gafalag.exception.ExpressionNotFound;
import org.lekitech.gafalag.projection.ExampleProjection;
import org.lekitech.gafalag.repository.v2.ExampleRepositoryV2;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.lekitech.gafalag.utils.SearchStringNormalizer.normalizeString;

/**
 * The `ExpressionServiceV2` class provides methods to interact with expressions and their details
 * in a version 2 of the application. It handles operations like searching for expressions, finding
 * expression suggestions, and retrieving expression details.
 * <p>
 * This service class is responsible for managing the business logic related to expressions and
 * acts as an intermediary between the data layer and the presentation layer.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExpressionServiceV2 {

    /* Dependencies */
    private final DictionaryMapper mapper;
    private final ExpressionRepositoryV2 expressionRepo;
    private final ExampleRepositoryV2 exampleRepo;

    /**
     * Retrieves a page of search suggestions based on the provided expression, source language,
     * and pageable information.
     *
     * @param spelling The expression to search for.
     * @param expLang  The source language for the expression.
     * @param size     The limit of the suggestions.
     * @return A List of strings containing search suggestions.
     */
    public List<SimilarDto> searchSuggestions(String spelling, String expLang, String defLang, Integer size) {
        final List<Expression> expressions = expressionRepo.fuzzySearchSpellingsListBySpellingAndExpLang(
                normalizeString(spelling),
                expLang,
                defLang,
                size
        );
        return mapper.toDto(expressions);
    }

    @Transactional
    public ExpressionAndSimilar getExpressionById(UUID id, String defLang, Integer size) {
        final Optional<Expression> expOptional = expressionRepo.findById(id);
        if (expOptional.isPresent()) {
            final Expression expression = expOptional.get();
            return getExpressionAndSimilar(expression, defLang, size);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public ExpressionAndSimilar getExpressionBySpellingAndSimilarAndExpLangAndDefLang(String spelling,
                                                                                      String expLang,
                                                                                      String defLang,
                                                                                      Integer size) {
        final Optional<Expression> expOptional = expressionRepo.findExpressionBySpellingAndLanguageAndDefLanguage(
                normalizeString(spelling),
                expLang,
                defLang
        );
        if (expOptional.isPresent()) {
            final Expression expression = expOptional.get();
            return getExpressionAndSimilar(expression, defLang, size);
        } else {
            final List<SimilarDto> similarDtos = searchSuggestions(spelling, expLang, defLang, size);
            return new ExpressionAndSimilar(null, similarDtos);
        }
    }

    private ExpressionAndSimilar getExpressionAndSimilar(Expression expression, String defLang, Integer size) {
        final List<ExpressionDetails> expressionDetails = expression.getExpressionDetails()
                .stream().map(expDetails -> {
                    val filteredDefinitionDetails = expDetails.getDefinitionDetails().stream().filter(
                            definitionDetails -> definitionDetails.getLanguage().getId().equals(defLang)
                    ).toList();
                    expDetails.setDefinitionDetails(filteredDefinitionDetails);
                    return expDetails;
                }).toList();
        final ExpressionResponseDto expressionResponseDto = mapper.toDto(expression.getId(), expression.getSpelling(), expressionDetails);
        final List<SimilarDto> similarDtos = searchSuggestions(expression.getSpelling(), expression.getLanguage().getId(), defLang, size);
        return new ExpressionAndSimilar(expressionResponseDto, similarDtos);
    }

    @Transactional
    public ExpressionResponseDto getExpressionOfTheDay(String currentDate) {
        try {
            final Optional<Expression> expressionOpt = expressionRepo.findExpressionByCurrentDate(Date.valueOf(currentDate));
            if (expressionOpt.isPresent()) {
                final Expression expression = expressionOpt.get();
                return mapper.toDto(expression.getId(), expression.getSpelling(), expression.getExpressionDetails());
            }
            return null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Incorrect date");
        }
    }

    @Transactional
    public List<ExpressionAndExampleDto> getExpressionAndExample(String searchString) throws ExpressionNotFound {
        final List<ExampleProjection> exampleProjection = exampleRepo.findExpressionAndExample(normalizeString(searchString));
        if (exampleProjection.isEmpty()) {
            throw new ExpressionNotFound("Not found: " + searchString);
        }
        record TempExpression(UUID id, String spelling) {
        }
        return exampleProjection.stream().map(expPrj -> {
                            TempExpression key = new TempExpression(expPrj.getExpressionId(), expPrj.getExpressionSpelling());
                            ExampleDto value = mapper.toDto(expPrj);
                            return Map.entry(key, value);
                        }
                ).collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue,
                                toList())))
                .entrySet().stream().map(entry -> new ExpressionAndExampleDto(
                                entry.getKey().id,
                                entry.getKey().spelling,
                                new ArrayList<>(entry.getValue())
                        )
                ).collect(toList());
    }

    @Transactional
    public List<ExpressionResponseDto> getExpressionsByTagAndDefLang(String tag, String defLang) {
        final List<Expression> expressions = expressionRepo.findExpressionsByTagAndDefLang(tag, defLang);
        return expressions.stream()
                .map(expression -> {
                    final List<ExpressionDetails> expressionDetails = expression.getExpressionDetails();
                    ExpressionExample filteredExpressionExamples = expressionDetails.stream()
                            .flatMap(expDetails -> expDetails.getExpressionExamples().stream())
                            .filter(expExample -> expExample.getExample().getExampleTags()
                                    .stream()
                                    .anyMatch(exampleTag -> exampleTag.getExampleTagId().getTagAbbr().equals(tag)))
                            .findFirst()
                            .orElse(null);
                    DefinitionDetails filteredDefinitionDetails = expressionDetails.stream()
                            .flatMap(expDetails -> expDetails.getDefinitionDetails().stream())
                            .filter(defDetails -> defDetails.getDefinitionDetailsTags().stream()
                                    .anyMatch(defDetailsTag -> defDetailsTag.getDefinitionDetailsTagId().getTagAbbr().equals(tag))
                            )
                            .findFirst().orElse(null);
                    Definition filteredDefinition = expressionDetails.stream()
                            .flatMap(expDetails -> expDetails.getDefinitionDetails().stream())
                            .flatMap(defDetails -> defDetails.getDefinitions().stream())
                            .filter(definition -> definition.getDefinitionTags()
                                    .stream()
                                    .anyMatch(definitionTag -> definitionTag.getDefinitionTagId().getTagAbbr().equals(tag)))
                            .findFirst()
                            .orElse(null);
                    DefinitionExample filteredDefinitionExamples = expressionDetails.stream()
                            .flatMap(expDetails -> expDetails.getDefinitionDetails().stream())
                            .flatMap(defDetails -> defDetails.getDefinitionExamples().stream())
                            .filter(example -> example.getExample().getExampleTags()
                                    .stream()
                                    .anyMatch(exampleTag -> exampleTag.getExampleTagId().getTagAbbr().equals(tag)))
                            .findFirst()
                            .orElse(null);
                   /*
                    List<ExpressionDetails> filteredExpressionDetails = getFilteredExpressionDetails(
                            expressionDetails,
                            filteredExpressionExamples,
                            filteredDefinitionDetails,
                            filteredDefinition,
                            filteredDefinitionExamples
                    );
                    expression.setExpressionDetails(filteredExpressionDetails);

                    */
                    return mapper.toDto(expression.getId(), expression.getSpelling(), expression.getExpressionDetails());
                }).collect(Collectors.toList());
    }

//    private List<ExpressionDetails> getFilteredExpressionDetails(List<ExpressionDetails> expressionDetails,
//                                                                 ExpressionExample filteredExpressionExamples,
//                                                                 DefinitionDetails filteredDefinitionDetails,
//                                                                 Definition filteredDefinition,
//                                                                 DefinitionExample filteredDefinitionExamples) {
//
//        return expressionDetails.stream().map()
//    }
}