package org.lekitech.gafalag.controller.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.*;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.exception.ExpressionNotFound;
import org.lekitech.gafalag.projection.DefinitionProjection;
import org.lekitech.gafalag.projection.ExampleProjection;
import org.lekitech.gafalag.service.v2.ExpressionServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * The `ExpressionControllerV2` class serves as the controller for managing and handling
 * expressions-related HTTP requests in a version 2 of the application. It provides endpoints
 * for listing expressions and retrieving search suggestions.
 * <p>
 * This controller class maps incoming HTTP requests to corresponding methods and manages
 * the interaction between the client and the `ExpressionServiceV2` to process and respond
 * to the requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v2/expressions")
public class ExpressionControllerV2 {

    /* Dependencies */
    private final ExpressionServiceV2 expService;

    /**
     * Endpoint for retrieving search suggestions based on the provided expression and source language.
     *
     * @param spelling The expression to search for.
     * @param expLang  The source language for the expression.
     * @param size     The limit of the suggestions (default is 10).
     * @return a ResponseEntity containing a List of search suggestions as strings.
     */
    @GetMapping(path = "/search/suggestions")
    public ResponseEntity<List<SimilarDto>> searchSuggestions(
            @RequestParam(name = "spelling") String spelling,
            @RequestParam(name = "expLang") String expLang,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        try {
            if (size > 50) {
                throw new IllegalArgumentException("similar count cannot be greater than 50");
            }
            final List<SimilarDto> suggestions = expService.getSuggestions(spelling, expLang, defLang, size);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            log.error("Error occurred while retrieving search suggestions: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(List.of());
        }
    }

    /**
     * Endpoint to search for an expression and similar expressions based on a given ID and source language.
     *
     * @param id           ID of the expression being searched for
     * @param defLang      The source language for the expression.
     * @param similarCount The limit of the suggestions (default is 10).
     * @return a ExpressionAndSimilar containing the found expression and similar expressions.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpressionAndSimilarDto> searchExpressionById(
            @PathVariable(name = "id") UUID id,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "similarCount", defaultValue = "10") Integer similarCount) {
        try {
            if (similarCount > 50) {
                throw new IllegalArgumentException("similarCount cannot be greater than 50");
            }
            final ExpressionAndSimilarDto expressionAndSimilar = expService.getExpressionById(id, defLang, similarCount);
            return ResponseEntity.ok(expressionAndSimilar);
        } catch (Exception e) {
            log.error("Error occurred while retrieving search expression and similar: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to search for an expression and similar expressions based on a given spelling and source language.
     *
     * @param spelling     The expression to search for.
     * @param expLang      The source language of the search expression.
     * @param defLang      The source language of the definition.
     * @param similarCount The limit of the suggestions (default is 10).
     * @return a ExpressionAndSimilarDto containing the found expression and similar expressions.
     */
    @GetMapping("/search")
    public ResponseEntity<ExpressionAndSimilarDto> searchExpressionAndSimilarBySpellingAndLangOfExpAndDef(
            @RequestParam(name = "spelling") String spelling,
            @RequestParam(name = "expLang") String expLang,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "similarCount", defaultValue = "10") Integer similarCount) {
        if (similarCount > 50) {
            throw new IllegalArgumentException("similarCount cannot be greater than 50");
        }
        final ExpressionAndSimilarDto expressionAndSimilar
                = expService.getExpressionAndSimilarBySpellingAndLangOfExpAndDef(spelling, expLang, defLang, similarCount);
        return ResponseEntity.ok(expressionAndSimilar);
    }

    /**
     * Endpoint to get a random expression based on the date.
     *
     * @param currentDate The source date to get the expression in the format yyy-MM-dd.
     * @return a ExpressionResponseDto containing the found expression.
     * @throws IllegalArgumentException if the date is entered in an invalid format.
     */
    @GetMapping("/day")
    public ResponseEntity<ExpressionResponseDto> searchWordOfTheDay(
            @RequestParam(name = "currentDate") String currentDate) {
        final ExpressionResponseDto expressionResponse = expService.getExpressionOfTheDay(currentDate);
        return ResponseEntity.ok(expressionResponse);
    }

    /**
     * Endpoint to retrieve a paginated list of examples based on the search string
     * and the expression language searched for.
     *
     * @param searchString The search string to match examples.
     * @param expLang      The language of the expressions.
     * @param pageSize     The number of examples per page (default is 10).
     * @param currentPage  The current page number (default is 0).
     * @return a ResponseEntity object containing a PaginationResponseDto with ExpressionAndExampleDto data.
     */
    @GetMapping("/search/examples")
    public ResponseEntity<PaginationResponseDto<ExampleProjection, ExpressionAndExampleDto>> findExamplesBySearchString(
            @RequestParam(name = "searchString") String searchString,
            @RequestParam(name = "expLang") String expLang,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "currentPage", defaultValue = "0") Integer currentPage) throws Exception {
        final PaginationResponseDto<ExampleProjection, ExpressionAndExampleDto> paginationResponse
                = expService.getExpressionsAndExamples(searchString, expLang, pageSize, currentPage);
        return ResponseEntity.ok(paginationResponse);
    }

    /**
     * Endpoint to retrieve a paginated list of expressions and definitions based on the search string
     * and expression language.
     *
     * @param searchString The search string to search definitions.
     * @param expLang      The language of the expressions.
     * @param pageSize     The number of examples per page (default is 10).
     * @param currentPage  The current page number (default is 0).
     * @return a ResponseEntity object containing a PaginationResponseDto with ExpressionAndDefinitionDto data.
     */
    @GetMapping("/search/definitions")
    public ResponseEntity<PaginationResponseDto<DefinitionProjection, ExpressionAndDefinitionDto>> searchExpressionAndDefinitionsBySearchString(
            @RequestParam(name = "searchString") String searchString,
            @RequestParam(name = "expLang") String expLang,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "currentPage", defaultValue = "0") Integer currentPage) throws Exception {
        final PaginationResponseDto<DefinitionProjection, ExpressionAndDefinitionDto> paginationResponse
                = expService.getExpressionsAndDefinitions(searchString, expLang, pageSize, currentPage);
        return ResponseEntity.ok(paginationResponse);
    }

    /**
     * Endpoint to retrieve a paginated list of expressions based on the tag
     * and the expression language being searched.
     *
     * @param tag         The tag to search for.
     * @param expLang     The language of the expressions to search for.
     * @param pageSize    The number of expressions to return per page (default is 10).
     * @param currentPage The current page number (default is 0).
     * @return a ResponseEntity containing a PaginationResponseDto with ExpressionByTagDto items.
     * @throws ExpressionNotFound if no expressions are found for the given tag and language.
     */
    @GetMapping("/search/tags")
    public ResponseEntity<PaginationResponseDto<Expression, ExpressionByTagDto>> searchExpressionsByTagAndLang(
            @RequestParam(name = "tag") String tag,
            @RequestParam(name = "expLang") String expLang,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "currentPage", defaultValue = "0") Integer currentPage) throws ExpressionNotFound {
        final PaginationResponseDto<Expression, ExpressionByTagDto> paginationResponse
                = expService.getExpressionsByTagAndExpLang(tag, expLang, pageSize, currentPage);
        return ResponseEntity.ok(paginationResponse);
    }
}