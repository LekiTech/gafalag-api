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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            /* This IllegalArgumentException is thrown to indicate an error comment for clarity of the problem encountered. */
            throw new IllegalArgumentException("Incorrect date");
        }
    }

    @Transactional
    public List<ExpressionAndExampleDto> getExpressionAndExample(String searchString) throws ExpressionNotFound {
        if (searchString == null || searchString.isBlank()) {
            throw new IllegalArgumentException("Incorrect search string format");
        }
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
    public List<FoundByTagDto> getExpressionsByTagAndLang(String tag, String lang, Integer size, Integer page) throws ExpressionNotFound {
        final List<Expression> expressions = expressionRepo.findExpressionsByTagAndLang(tag, lang, size, page);
        if (expressions.isEmpty()) {
            throw new ExpressionNotFound("No expressions for the tag '" + tag + "' and the language '" + lang + "' were found!");
        }
        return expressions.stream()
                .map(expression -> {
                    final FoundByTagDto.ShortExampleDto expressionExample = findFirstExpressionExampleByTag(expression.getExpressionDetails(), tag);
                    if (expressionExample != null) {
                        return new FoundByTagDto(expression, expressionExample);
                    }
                    final List<DefinitionDetails> definitionDetails = expression.getExpressionDetails().stream()
                            .flatMap(expDetails -> expDetails.getDefinitionDetails().stream())
                            .toList();
                    final FoundByTagDto.ShortDefinitionDto shortDefinition = findFirstDefinitionByTag(definitionDetails, tag);
                    if (shortDefinition != null) {
                        return new FoundByTagDto(expression, shortDefinition);
                    }
                    final FoundByTagDto.ShortExampleDto definitionExample = findFirstDefinitionExampleByTag(definitionDetails, tag);
                    if (definitionExample != null) {
                        return new FoundByTagDto(expression, definitionExample);
                    }
                    return null;
                }).collect(Collectors.toList());
    }

    private FoundByTagDto.ShortExampleDto findFirstExpressionExampleByTag(List<ExpressionDetails> expressionDetails, String tag) {
        ExpressionExample filteredExpressionExamples = expressionDetails.stream()
                .flatMap(expDetails -> expDetails.getExpressionExamples().stream())
                .filter(expExample -> expExample.getExample().getExampleTags()
                        .stream()
                        .anyMatch(exampleTag -> exampleTag.getExampleTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
        if (filteredExpressionExamples != null) {
            Example example = filteredExpressionExamples.getExample();
            List<String> allTags = example.getExampleTags().stream().map(expTags -> expTags.getExampleTagId().getTagAbbr()).toList();
            return new FoundByTagDto.ShortExampleDto(example.getSource(), example.getTranslation(), allTags);
        }
        return null;
    }

    private FoundByTagDto.ShortDefinitionDto findFirstDefinitionByTag(List<DefinitionDetails> definitionDetails, String tag) {
        // Find first definition details that contains provided tag
        DefinitionDetails filteredDefinitionDetails = definitionDetails.stream()
                .filter(defDetails -> defDetails.getDefinitionDetailsTags().stream()
                        .anyMatch(defDetailsTag -> defDetailsTag.getDefinitionDetailsTagId().getTagAbbr().equals(tag))
                )
                .findFirst()
                .orElse(null);
        if (filteredDefinitionDetails != null) {
            var definitionDetailsTags = filteredDefinitionDetails.getDefinitionDetailsTags().stream()
                    .map(ddt -> ddt.getDefinitionDetailsTagId().getTagAbbr());
            var foundDefinition = filteredDefinitionDetails.getDefinitions().stream()
                    .findFirst()
                    .orElse(null);
            if (foundDefinition != null) {
                var definitionTags = foundDefinition.getDefinitionTags().stream()
                        .map(ddt -> ddt.getDefinitionTagId().getTagAbbr());
                List<String> allTags = Stream.concat(definitionDetailsTags, definitionTags).toList();
                return new FoundByTagDto.ShortDefinitionDto(foundDefinition.getValue(), allTags);
            }
        }
        // Find first definition that contains provided tag
        Definition foundDefinition = definitionDetails.stream()
                .flatMap(defDetails -> defDetails.getDefinitions().stream())
                .filter(definition -> definition.getDefinitionTags()
                        .stream()
                        .anyMatch(definitionTag -> definitionTag.getDefinitionTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
        if (foundDefinition != null) {
            List<String> definitionTags = foundDefinition.getDefinitionTags().stream()
                    .map(ddt -> ddt.getDefinitionTagId().getTagAbbr())
                    .toList();
            return new FoundByTagDto.ShortDefinitionDto(foundDefinition.getValue(), definitionTags);
        }
        return null;
    }

    private FoundByTagDto.ShortExampleDto findFirstDefinitionExampleByTag(List<DefinitionDetails> definitionDetails, String tag) {
        DefinitionDetails filteredDefinitionDetails = definitionDetails.stream()
                .filter(defDetails -> defDetails.getDefinitionDetailsTags().stream()
                        .anyMatch(defDetailsTag -> defDetailsTag.getDefinitionDetailsTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
        if (filteredDefinitionDetails != null
                && (filteredDefinitionDetails.getDefinitions() == null || filteredDefinitionDetails.getDefinitions().isEmpty())) {
            DefinitionExample filteredDefinitionExamples = filteredDefinitionDetails.getDefinitionExamples().stream()
                    .filter(example -> example.getExample().getExampleTags()
                            .stream()
                            .anyMatch(exampleTag -> exampleTag.getExampleTagId().getTagAbbr().equals(tag)))
                    .findFirst()
                    .orElse(null);
            if (filteredDefinitionExamples != null) {
                var definitionDetailsTags = filteredDefinitionDetails.getDefinitionDetailsTags().stream()
                        .map(defDetailsTag -> defDetailsTag.getDefinitionDetailsTagId().getTagAbbr());
                var exampleTags = filteredDefinitionExamples.getExample().getExampleTags().stream()
                        .map(defExampleTag -> defExampleTag.getExampleTagId().getTagAbbr());
                List<String> allTags = Stream.concat(definitionDetailsTags, exampleTags).toList();
                return new FoundByTagDto.ShortExampleDto(
                        filteredDefinitionExamples.getExample().getSource(),
                        filteredDefinitionExamples.getExample().getTranslation(),
                        allTags
                );
            }
        }
        DefinitionExample filteredDefinitionExample = definitionDetails.stream()
                .flatMap(defDetail -> defDetail.getDefinitionExamples().stream())
                .filter(example -> example.getExample().getExampleTags()
                        .stream()
                        .anyMatch(exampleTag -> exampleTag.getExampleTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
        if (filteredDefinitionExample != null) {
            List<String> exampleTags = filteredDefinitionExample.getExample().getExampleTags().stream()
                    .map(expTag -> expTag.getExampleTagId().getTagAbbr())
                    .toList();
            return new FoundByTagDto.ShortExampleDto(
                    filteredDefinitionExample.getExample().getSource(),
                    filteredDefinitionExample.getExample().getTranslation(),
                    exampleTags
            );
        }
        return null;
    }
}