package org.lekitech.gafalag.controller.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.*;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.exception.ExpressionNotFound;
import org.lekitech.gafalag.projection.DefinitionProjection;
import org.lekitech.gafalag.projection.ExampleProjection;
import org.lekitech.gafalag.service.v2.ExpressionServiceV2;
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
     * Endpoint for retrieving search suggestions based on the provided expression and language of expression.
     *
     * @param spelling The expression to search for.
     * @param expLang  The language of the expression.
     * @param size     The limit of the suggestions (default is 10).
     * @return a ResponseEntity containing a List of search suggestions as strings.
     */
    @GetMapping(path = "/search/suggestions")
    public ResponseEntity<List<SimilarDto>> searchSuggestions(
            @RequestParam(name = "spelling") String spelling,
            @RequestParam(name = "expLang") String expLang,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        if (size > 50) {
            throw new IllegalArgumentException("similar count cannot be greater than 50");
        }
        final List<SimilarDto> suggestions = expService.searchSuggestions(spelling, expLang, defLang, size);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Endpoint to get for an expression and similar expressions based on a given ID and language of definition.
     *
     * @param id           ID of the expression being searched for
     * @param defLang      The language of the definition.
     * @param similarCount The limit of the suggestions (default is 10).
     * @return a ExpressionAndSimilar containing the found expression and similar expressions.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpressionAndSimilarDto> getExpressionById(
            @PathVariable(name = "id") UUID id,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "similarCount", defaultValue = "10") Integer similarCount) {
        if (similarCount > 50) {
            throw new IllegalArgumentException("similarCount cannot be greater than 50");
        }
        final ExpressionAndSimilarDto expressionAndSimilar = expService.getExpressionById(id, defLang, similarCount);
        return ResponseEntity.ok(expressionAndSimilar);
    }

    /**
     * Endpoint to search for an expression and similar expressions based on a given spelling and languages
     * of expression and definition.
     *
     * @param spelling     The expression to search for.
     * @param expLang      The language of the expression.
     * @param defLang      The language of the definition.
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
                = expService.searchExpressionAndSimilarBySpellingAndLangOfExpAndDef(spelling, expLang, defLang, similarCount);
        return ResponseEntity.ok(expressionAndSimilar);
    }

    /**
     * Endpoint to get a random expression based on the date.
     *
     * @param currentDate The current date for which we are getting a unique expression in the format yyy-MM-dd.
     * @return a ExpressionResponseDto containing the found expression.
     * @throws IllegalArgumentException if the date is entered in an invalid format.
     */
    @GetMapping("/day")
    public ResponseEntity<ExpressionResponseDto> getWordOfTheDay(
            @RequestParam(name = "currentDate") String currentDate) {
        final ExpressionResponseDto expressionResponse = expService.getExpressionOfTheDay(currentDate);
        return ResponseEntity.ok(expressionResponse);
    }

    /**
     * Endpoint to retrieve a paginated list of examples based on the search string
     * and the expression language searched for, and it's also possible to filter by tag.
     *
     * @param searchString The search string to match examples.
     * @param exampleLang  The language of the 'example source' or 'example translation'.
     * @param pageSize     The number of examples per page (default is 10).
     * @param currentPage  The current page number (default is 0).
     * @param tag          The tag to search for (default is null).
     * @return a ResponseEntity object containing a PaginationResponseDto with ExpressionAndExampleDto data.
     */
    @GetMapping("/search/examples")
    public ResponseEntity<PaginationResponseDto<ExampleProjection, ExpressionAndExampleDto>> searchExamplesBySearchString(
            @RequestParam(name = "searchString") String searchString,
            @RequestParam(name = "exampleLang") String exampleLang,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(name = "tag", required = false) String tag) throws Exception {
        final PaginationResponseDto<ExampleProjection, ExpressionAndExampleDto> paginationResponse
                = expService.searchExpressionsAndExamples(searchString, exampleLang, pageSize, currentPage, tag);
        return ResponseEntity.ok(paginationResponse);
    }

    /**
     * Endpoint to retrieve a paginated list of expressions and definitions based on the search string
     * and expression language, and it's also possible to filter by tag.
     *
     * @param searchString The search string to search definitions.
     * @param defLang      The language of the definition.
     * @param pageSize     The number of examples per page (default is 10).
     * @param currentPage  The current page number (default is 0).
     * @param tag          The tag to search for (default is null).
     * @return a ResponseEntity object containing a PaginationResponseDto with ExpressionAndDefinitionDto data.
     */
    @GetMapping("/search/definitions")
    public ResponseEntity<PaginationResponseDto<DefinitionProjection, ExpressionAndDefinitionDto>> searchDefinitionsBySearchString(
            @RequestParam(name = "searchString") String searchString,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(name = "tag", required = false) String tag) throws Exception {
        final PaginationResponseDto<DefinitionProjection, ExpressionAndDefinitionDto> paginationResponse
                = expService.searchExpressionsAndDefinitions(searchString, defLang, pageSize, currentPage, tag);
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
                = expService.searchExpressionsByTagAndExpLang(tag, expLang, pageSize, currentPage);
        return ResponseEntity.ok(paginationResponse);
    }
}