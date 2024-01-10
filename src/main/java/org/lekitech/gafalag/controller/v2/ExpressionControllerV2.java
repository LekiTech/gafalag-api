package org.lekitech.gafalag.controller.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.service.v2.ExpressionServiceV2;
import org.lekitech.gafalag.utils.Transliterator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // Dependencies
    private final ExpressionServiceV2 expService;

    /**
     * Endpoint for listing expressions with paging support.
     *
     * @param spelling The spelling of the expression to search for.
     * @param srcLang  The source language for the expression.
     * @param distLang The destination language for expression details.
     * @param pageable The pageable information for the search results.
     * @return A ResponseEntity containing a Page of ExpressionResponseDto objects.
     */
    @GetMapping("/pages")
    public ResponseEntity<Page<ExpressionResponseDto>> listExpressions(
            @RequestParam(name = "spelling") String spelling,
            @RequestParam(name = "expLang") String srcLang,
            @RequestParam(name = "defLang") String distLang,
            Pageable pageable) {
        try {
            final String checkedExp = Transliterator.translitToCyrillic(spelling, srcLang);
            final Page<ExpressionResponseDto> expressions = expService
                    .findExpressionsBySpellingAndSrcLang(checkedExp, srcLang, distLang, pageable);
            return ResponseEntity.ok(expressions);
        } catch (Exception e) {
            log.error("Failed to fetch expression pages: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Page.empty());
        }
    }

    /**
     * Endpoint for retrieving search suggestions based on the provided expression and source language.
     *
     * @param exp      The expression to search for.
     * @param srcLang  The source language for the expression.
     * @param pageable The pageable information for the search suggestions.
     * @return A ResponseEntity containing a Page of search suggestions as strings.
     */
    @GetMapping("/search/suggestions")
    public ResponseEntity<Page<String>> searchSuggestions(
            @RequestParam(name = "exp") String exp,
            @RequestParam(name = "expLang") String srcLang,
            Pageable pageable) {
        try {
            final String checkedExp = Transliterator.translitToCyrillic(exp, srcLang);
            final Page<String> suggestions = expService.searchSuggestions(checkedExp, srcLang, pageable);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            log.error("Error occurred while retrieving search suggestions: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Page.empty());
        }
    }
}
