package org.lekitech.gafalag.controller.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.ExpressionAndSimilar;
import org.lekitech.gafalag.dto.v2.SimilarDto;
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
     * @param size     The limit of the suggestions.
     * @return A ResponseEntity containing a List of search suggestions as strings.
     */
    @GetMapping(path = "/search/suggestions")
    public ResponseEntity<List<SimilarDto>> searchSuggestions(
            @RequestParam(name = "spelling") String spelling,
            @RequestParam(name = "expLang") String expLang,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "size") Long size) {
        try {
            final List<SimilarDto> suggestions = expService.searchSuggestions(spelling, expLang, defLang, size);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            log.error("Error occurred while retrieving search suggestions: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpressionAndSimilar> getExpressionByIdAndSimilar(
            @PathVariable(name = "id") UUID id,
            @RequestParam(name = "defLang") String defLang,
            @RequestParam(name = "size", defaultValue = "10") Long size) {
        try {
            final ExpressionAndSimilar expAndSimilar = expService.getExpressionByIdAndSimilar(id, defLang, size);
            return ResponseEntity.ok(expAndSimilar);
        } catch (Exception e) {
            log.error("Error occurred while retrieving search expression and similar: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}