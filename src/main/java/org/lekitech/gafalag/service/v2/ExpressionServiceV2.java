package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.*;
import org.lekitech.gafalag.dto.v2.mapper.DictionaryMapper;
import org.lekitech.gafalag.entity.v2.*;
import org.lekitech.gafalag.exception.ExpressionNotFound;
import org.lekitech.gafalag.projection.DefinitionProjection;
import org.lekitech.gafalag.projection.ExampleProjection;
import org.lekitech.gafalag.repository.v2.DefinitionRepositoryV2;
import org.lekitech.gafalag.repository.v2.ExampleRepositoryV2;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final DefinitionRepositoryV2 definitionRepo;

    /**
     * Retrieves a page of search suggestions based on the provided expression, language of expression,
     * and pageable information.
     *
     * @param spelling The expression to search for.
     * @param expLang  The language of the expression.
     * @param size     The limit of the suggestions.
     * @return a List of strings containing search suggestions.
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

    /**
     * The method returns the expression and similar expressions based on the provided expression ID,
     * the definition language, and the number of similar expressions.
     *
     * @param id           A unique identifier to search for the expression.
     * @param defLang      The language of the definition.
     * @param similarCount The number of similar expressions to retrieve along with the main Expression.
     * @return an {@link ExpressionAndSimilarDto} object containing the main Expression and its similar expressions.
     * @throws IllegalArgumentException if the expression with the given ID is not found.
     */
    @Transactional
    public ExpressionAndSimilarDto getExpressionById(UUID id, String defLang, Integer similarCount) {
        final Optional<Expression> expOptional = expressionRepo.findById(id);
        if (expOptional.isPresent()) {
            final Expression expression = expOptional.get();
            return getExpressionAndSimilar(expression, defLang, similarCount);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * The method returns the expression and similar data based on the provided spelling, expression language,
     * definition language and count of similar expressions.
     *
     * @param spelling     The spelling of the expression to retrieve.
     * @param expLang      The language of the expression.
     * @param defLang      The language of the definition.
     * @param similarCount The number of similar expressions to include.
     * @return an {@link ExpressionAndSimilarDto} object containing data for the requested expression and its similar
     * expressions or a list of suggested similar expressions if the expression is not found.
     */
    @Transactional
    public ExpressionAndSimilarDto searchExpressionAndSimilarBySpellingAndLangOfExpAndDef(String spelling,
                                                                                          String expLang,
                                                                                          String defLang,
                                                                                          Integer similarCount) {
        final Optional<Expression> expOptional = expressionRepo.findExpressionBySpellingAndLanguageAndDefLanguage(
                normalizeString(spelling),
                expLang,
                defLang
        );
        if (expOptional.isPresent()) {
            final Expression expression = expOptional.get();
            return getExpressionAndSimilar(expression, defLang, similarCount);
        } else {
            final List<SimilarDto> similarDtos = searchSuggestions(spelling, expLang, defLang, similarCount);
            return new ExpressionAndSimilarDto(null, similarDtos);
        }
    }

    /**
     * The private method returns the expression and similar expressions based on the provided Expression object,
     * definition language and count of similar expressions.
     *
     * @param expression   The Expression object for which details and similar expressions are to be retrieved.
     * @param defLang      The language of the definition.
     * @param similarCount The number of similar expressions to include.
     * @return an {@link ExpressionAndSimilarDto} object containing details of the provided Expression and a list
     * of similar expressions based on the specified criteria.
     */
    private ExpressionAndSimilarDto getExpressionAndSimilar(Expression expression, String defLang, Integer similarCount) {
        final List<ExpressionDetails> expressionDetails = expression.getExpressionDetails()
                .stream().map(expDetails -> {
                    val filteredDefinitionDetails = expDetails.getDefinitionDetails().stream().filter(
                            definitionDetails -> definitionDetails.getLanguage().getId().equals(defLang)
                    ).toList();
                    expDetails.setDefinitionDetails(filteredDefinitionDetails);
                    return expDetails;
                }).toList();
        final ExpressionResponseDto expressionResponseDto = mapper.toDto(expression.getId(), expression.getSpelling(), expressionDetails);
        final List<SimilarDto> similarDtos = searchSuggestions(expression.getSpelling(), expression.getLanguage().getId(), defLang, similarCount);
        return new ExpressionAndSimilarDto(expressionResponseDto, similarDtos);
    }

    /**
     * The method returns the expression of the day based on the provided current date.
     *
     * @param currentDate The current date in the format "yyyy-MM-dd".
     * @return an {@link ExpressionResponseDto} object containing details of the expression of the day,
     * or null if no expression is found for the given date.
     * @throws IllegalArgumentException if the provided date format is incorrect or an error occurs while processing.
     */
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

    /**
     * Checks the search string, language, page size, and current page parameters to see if they match.
     *
     * @param searchString The search string to be validated.
     * @param lang         The language to be validated.
     * @param pageSize     The page size to be validated.
     * @param currentPage  The current page number to be validated.
     * @throws IllegalArgumentException  if the search string is null or empty.
     * @throws IndexOutOfBoundsException if the page size is invalid or exceeds the maximum allowed value,
     *                                   or if the current page number exceeds the maximum allowed value.
     */
    private Integer inputValidation(String searchString, String lang, Integer pageSize, Integer currentPage) {
        if (searchString == null || searchString.isBlank()) {
            throw new IllegalArgumentException("Incorrect search string format.");
        } else if (lang == null || lang.isBlank()) {
            throw new IllegalArgumentException("Incorrect language format.");
        } else if (pageSize <= 0 || pageSize > 50) {
            throw new IndexOutOfBoundsException("Incorrect page size format or page size exceeds maximum.");
        } else if (currentPage >= 99999) {
            throw new IndexOutOfBoundsException("Page exceeds maximum.");
        } else if (currentPage < 0) {
            return 0;
        }
        return currentPage;
    }

    /**
     * The method returns a paginated list of {@link ExpressionAndExampleDto} based on the search string,
     * expression language, and pagination parameters. And it's also possible to filter by tag.
     *
     * @param searchString The string to search for within expressions.
     * @param exampleLang  The language of the 'example source' or 'example translation'.
     * @param pageSize     The size of each page in the pagination.
     * @param currentPage  The current page number of the pagination.
     * @param tag          The tag to search for (default is null).
     * @return a {@link PaginationResponseDto} with {@link ExpressionAndExampleDto} object.
     * @throws ExpressionNotFound        if no expressions matching the search criteria are found.
     * @throws IllegalArgumentException  if the search string or lang is null or empty.
     * @throws IndexOutOfBoundsException if the page size is less than 0 or greater than 50,
     *                                   or if the current page number is greater than and equal to 99_999.
     */
    @Transactional
    public PaginationResponseDto<ExampleProjection, ExpressionAndExampleDto> searchExpressionsAndExamples(String searchString,
                                                                                                          String exampleLang,
                                                                                                          Integer pageSize,
                                                                                                          Integer currentPage,
                                                                                                          String tag) throws ExpressionNotFound {
        currentPage = inputValidation(searchString, exampleLang, pageSize, currentPage);
        final Page<ExampleProjection> exampleProjectionPage
                = exampleRepo.findExpressionAndExample(normalizeString(searchString), exampleLang, PageRequest.of(currentPage, pageSize), tag);
        final List<ExampleProjection> exampleProjections = exampleProjectionPage.getContent();
        if (exampleProjections.isEmpty()) {
            throw new ExpressionNotFound("Not found: " + searchString);
        }
        List<ExpressionAndExampleDto> expAndExampleDtos = exampleProjections.stream()
                .map(exampleProjection -> new ExpressionAndExampleDto(
                                exampleProjection.getExpressionId(),
                                exampleProjection.getExpressionSpelling(),
                                mapper.toDto(exampleProjection)
                        )
                ).toList();
        return new PaginationResponseDto<>(exampleProjectionPage, expAndExampleDtos);
    }

    /**
     * The method returns a paginated list of {@link ExpressionAndDefinitionDto} based on the search string,
     * expression language, and pagination parameters. And it's also possible to filter by tag.
     *
     * @param searchString The string to search for within expressions.
     * @param defLang      The language of the definition to filter by.
     * @param pageSize     The size of each page in the pagination.
     * @param currentPage  The current page number of the pagination.
     * @param tag          The tag to search for (default is null).
     * @return a {@link PaginationResponseDto} with {@link ExpressionAndDefinitionDto} object.
     * @throws ExpressionNotFound        if no expressions matching the search criteria are found.
     * @throws IllegalArgumentException  if the search string or lang is null or empty.
     * @throws IndexOutOfBoundsException if the page size is less than 0 or greater than 50,
     *                                   or if the current page number is greater than and equal to 99_999.
     */
    @Transactional
    public PaginationResponseDto<DefinitionProjection, ExpressionAndDefinitionDto> searchExpressionsAndDefinitions(String searchString,
                                                                                                                   String defLang,
                                                                                                                   Integer pageSize,
                                                                                                                   Integer currentPage,
                                                                                                                   String tag) throws ExpressionNotFound {
        currentPage = inputValidation(searchString, defLang, pageSize, currentPage);
        final Page<DefinitionProjection> defProjectionPage
                = definitionRepo.findExpressionsAndDefinitions(normalizeString(searchString), defLang, PageRequest.of(currentPage, pageSize), tag);
        final List<DefinitionProjection> definitionProjections = defProjectionPage.getContent();
        if (definitionProjections.isEmpty()) {
            throw new ExpressionNotFound("Not found: " + searchString);
        }
        final List<ExpressionAndDefinitionDto> expAndDefDto = definitionProjections.stream()
                .map(definitionProjection -> new ExpressionAndDefinitionDto(
                                definitionProjection.getExpressionId(),
                                definitionProjection.getExpressionSpelling(),
                                mapper.toDto(definitionProjection)
                        )
                ).toList();
        return new PaginationResponseDto<>(defProjectionPage, expAndDefDto);
    }

    /**
     * The method returns a paginated list of {@link ExpressionByTagDto} based on the tag,
     * expression language, and pagination parameters.
     *
     * @param tag         The tag to search for within expressions.
     * @param expLang     The language of the expressions to filter by.
     * @param pageSize    The size of each page in the pagination.
     * @param currentPage The current page number of the pagination.
     * @return a {@link PaginationResponseDto} with {@link ExpressionByTagDto} object.
     * @throws ExpressionNotFound        if no expressions matching the search criteria are found.
     * @throws IllegalArgumentException  if the tag or lang is null or empty.
     * @throws IndexOutOfBoundsException if the page size is less than 0 or greater than 50,
     *                                   or if the current page number is greater than and equal to 99_999.
     */
    @Transactional
    public PaginationResponseDto<Expression, ExpressionByTagDto> searchExpressionsByTagAndExpLang(String tag,
                                                                                                  String expLang,
                                                                                                  Integer pageSize,
                                                                                                  Integer currentPage) throws ExpressionNotFound {
        currentPage = inputValidation(tag, expLang, pageSize, currentPage);
        final Page<Expression> expressionsPage
                = expressionRepo.findExpressionsByTagAndExpLang(tag, expLang, PageRequest.of(currentPage, pageSize));
        final List<Expression> expressions = expressionsPage.getContent();
        if (expressions.isEmpty()) {
            throw new ExpressionNotFound("No expressions for the tag '" + tag + "' and the language '" + expLang + "' were found!");
        }
        List<ExpressionByTagDto> expressionsByTagDto = expressions.stream()
                .map(expression -> {
                    final ExpressionByTagDto.ShortExampleDto expressionExample = findFirstExpressionExampleByTag(expression.getExpressionDetails(), tag);
                    if (expressionExample != null) {
                        return new ExpressionByTagDto(expression, expressionExample);
                    }
                    final List<DefinitionDetails> definitionDetails = expression.getExpressionDetails().stream()
                            .flatMap(expDetails -> expDetails.getDefinitionDetails().stream()).toList();
                    final ExpressionByTagDto.ShortDefinitionDto shortDefinition = findFirstDefinitionByTag(definitionDetails, tag);
                    if (shortDefinition != null) {
                        return new ExpressionByTagDto(expression, shortDefinition);
                    }
                    final ExpressionByTagDto.ShortExampleDto definitionExample = findFirstDefinitionExampleByTag(definitionDetails, tag);
                    if (definitionExample != null) {
                        return new ExpressionByTagDto(expression, definitionExample);
                    }
                    return null;
                }).collect(Collectors.toList());
        return new PaginationResponseDto<>(expressionsPage, expressionsByTagDto);
    }

    /**
     * Finds the first ExpressionExample filtered by a specific tag from a list of ExpressionDetails.
     *
     * @param expressionDetails The list of ExpressionDetails to search within.
     * @param tag               The tag to filter by in the ExpressionExamples.
     * @return {@link ExpressionByTagDto.ShortExampleDto} containing the first filtered ExpressionExample
     * and its associated tags, or null if not found.
     * This method iterates through the provided expressionDetails list and searches for an expression example
     * that contains the specified tag. If found, it constructs and returns a ShortExampleDto object representing
     * the example along with all its associated tags. If no example with the specified tag is found, null is returned.
     */
    private ExpressionByTagDto.ShortExampleDto findFirstExpressionExampleByTag(List<ExpressionDetails> expressionDetails, String tag) {
        ExpressionExample filteredExpressionExample = expressionDetails.stream()
                .flatMap(expDetails -> expDetails.getExpressionExamples().stream())
                .filter(expExample -> expExample.getExample().getExampleTags()
                        .stream()
                        .anyMatch(exampleTag -> exampleTag.getExampleTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
        if (filteredExpressionExample != null) {
            Example example = filteredExpressionExample.getExample();
            List<String> allExampleTags = example.getExampleTags().stream()
                    .map(expTags -> expTags.getExampleTagId().getTagAbbr())
                    .toList();
            return new ExpressionByTagDto.ShortExampleDto(example, allExampleTags);
        }
        return null;
    }

    /**
     * Finds the first definition associated with a specific tag from a list of definition-details or definitions.
     *
     * @param definitionDetails A list of DefinitionDetails objects containing definitions and their associated tags.
     * @param tag               The tag abbreviation to search for within the definition details or definitions.
     * @return a {@link ExpressionByTagDto.ShortDefinitionDto} object representing the first definition found
     * with the given tag, or null if not found.
     * This method first attempts to find a definition-details object that contains the provided tag.
     * If found, it retrieves the first definition from that definition-details along with all its associated tags
     * and constructs a ShortDefinitionDto object representing it. If no definition-details with the specified
     * tag is found, it then searches for individual definitions that contain the provided tag. If found,
     * it constructs a ShortDefinitionDto object representing the definition along with its associated tags.
     * If no definition with the specified tag is found, null is returned.
     */
    private ExpressionByTagDto.ShortDefinitionDto findFirstDefinitionByTag(List<DefinitionDetails> definitionDetails, String tag) {
        // Find first definition-details that contains provided tag
        DefinitionDetails filteredDefDetails = getFilteredDefDetails(definitionDetails, tag);
        if (filteredDefDetails != null) {
            var definitionDetailsTags = filteredDefDetails.getDefinitionDetailsTags().stream()
                    .map(ddt -> ddt.getDefinitionDetailsTagId().getTagAbbr());
            var filteredDefinition = filteredDefDetails.getDefinitions().stream()
                    .findFirst()
                    .orElse(null);
            if (filteredDefinition != null) {
                var definitionTags = filteredDefinition.getDefinitionTags().stream()
                        .map(ddt -> ddt.getDefinitionTagId().getTagAbbr());
                List<String> allTags = Stream.concat(definitionDetailsTags, definitionTags).toList();
                return new ExpressionByTagDto.ShortDefinitionDto(filteredDefinition.getValue(), allTags);
            }
        }
        // Find first definition that contains provided tag
        Definition filteredDefinition = definitionDetails.stream()
                .flatMap(defDetails -> defDetails.getDefinitions().stream())
                .filter(definition -> definition.getDefinitionTags()
                        .stream()
                        .anyMatch(definitionTag -> definitionTag.getDefinitionTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
        if (filteredDefinition != null) {
            List<String> definitionTags = filteredDefinition.getDefinitionTags().stream()
                    .map(ddt -> ddt.getDefinitionTagId().getTagAbbr())
                    .toList();
            return new ExpressionByTagDto.ShortDefinitionDto(filteredDefinition.getValue(), definitionTags);
        }
        return null;
    }

    /**
     * Finds the first example associated with a specific tag from a list of definition details or definition examples.
     *
     * @param definitionDetails A list of DefinitionDetails objects containing definition examples and their associated tags.
     * @param tag               The tag abbreviation to search for within the definition details or definition examples.
     * @return a {@link ExpressionByTagDto.ShortExampleDto} object representing the first example found with the given tag, or null if not found.
     * This method first attempts to find a definition-details object that contains the provided tag. If found, it checks if the
     * definitions list is empty. If it's empty, it looks for a definition example that contains the provided tag within that
     * definition-details. If found, it constructs a ShortExampleDto object representing the example along with all its associated
     * tags. If no definition example with the specified tag is found, it then searches for individual definition examples that
     * contain the provided tag across all definition details. If found, it constructs a ShortExampleDto object representing the
     * example along with its associated tags. If no example with the specified tag is found, null is returned.
     */
    private ExpressionByTagDto.ShortExampleDto findFirstDefinitionExampleByTag(List<DefinitionDetails> definitionDetails, String tag) {
        DefinitionDetails filteredDefDetails = getFilteredDefDetails(definitionDetails, tag);
        if (filteredDefDetails != null && (filteredDefDetails.getDefinitions() == null || filteredDefDetails.getDefinitions().isEmpty())) {
            DefinitionExample filteredDefExample = getFilteredDefExample(filteredDefDetails.getDefinitionExamples().stream().toList(), tag);
            if (filteredDefExample != null) {
                var definitionDetailsTags = filteredDefDetails.getDefinitionDetailsTags().stream()
                        .map(defDetailsTag -> defDetailsTag.getDefinitionDetailsTagId().getTagAbbr());
                var exampleTags = filteredDefExample.getExample().getExampleTags().stream()
                        .map(defExampleTag -> defExampleTag.getExampleTagId().getTagAbbr());
                List<String> allTags = Stream.concat(definitionDetailsTags, exampleTags).toList();
                return new ExpressionByTagDto.ShortExampleDto(filteredDefExample.getExample(), allTags);
            }
        }
        DefinitionExample filteredDefExample = getFilteredDefExample(definitionDetails.stream().flatMap(defDetail ->
                defDetail.getDefinitionExamples().stream()).toList(), tag);
        if (filteredDefExample != null) {
            List<String> exampleTags = filteredDefExample.getExample().getExampleTags().stream()
                    .map(expTag -> expTag.getExampleTagId().getTagAbbr())
                    .toList();
            return new ExpressionByTagDto.ShortExampleDto(filteredDefExample.getExample(), exampleTags);
        }
        return null;
    }

    /**
     * Filters and retrieves the first {@link DefinitionDetails} object from a list of {@link DefinitionDetails}
     * based on the presence of a specific tag.
     *
     * @param definitionDetails A list of {@link DefinitionDetails} objects to filter.
     * @param tag               The tag abbreviation to search for within the {@link DefinitionDetails} objects.
     * @return the first {@link DefinitionDetails} object that contains the provided tag, or null if not found.
     */
    private DefinitionDetails getFilteredDefDetails(List<DefinitionDetails> definitionDetails, String tag) {
        return definitionDetails.stream()
                .filter(defDetails -> defDetails.getDefinitionDetailsTags().stream()
                        .anyMatch(defDetailsTag -> defDetailsTag.getDefinitionDetailsTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filters and retrieves the first {@link DefinitionExample} object from a list of {@link DefinitionExample}
     * based on the presence of a specific tag.
     *
     * @param definitionExamples A list of {@link DefinitionExample} objects to filter.
     * @param tag                The tag abbreviation to search for within the {@link DefinitionExample} objects.
     * @return the first {@link DefinitionExample} object that contains the provided tag, or null if not found.
     */
    private DefinitionExample getFilteredDefExample(List<DefinitionExample> definitionExamples, String tag) {
        return definitionExamples.stream()
                .filter(example -> example.getExample().getExampleTags()
                        .stream()
                        .anyMatch(exampleTag -> exampleTag.getExampleTagId().getTagAbbr().equals(tag)))
                .findFirst()
                .orElse(null);
    }
}