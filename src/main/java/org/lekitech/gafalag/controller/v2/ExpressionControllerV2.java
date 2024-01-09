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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v2/expressions")
public class ExpressionControllerV2 {

    private final ExpressionServiceV2 expressionServiceV2;

    @GetMapping("/pages")
    public ResponseEntity<Page<ExpressionResponseDto>> listExpressions(@RequestParam String spelling,
                                                                       @RequestParam String srcLang,
                                                                       Pageable pageable) {
        try {
            final String checkedExp = Transliterator.translitToCyrillic(spelling, srcLang);
            final Page<ExpressionResponseDto> expressions = expressionServiceV2
                    .findExpressionsBySpellingAndSrcLang(checkedExp, srcLang, pageable);
            return ResponseEntity.ok(expressions);
        } catch (Exception e) {
            log.error("Failed to fetch expression pages: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Page.empty());
        }
    }

    @GetMapping("/search/suggestions")
    public ResponseEntity<Page<String>> searchSuggestions(@RequestParam String exp,
                                                          @RequestParam String srcLang,
                                                          Pageable pageable) {
        try {
            final String checkedExp = Transliterator.translitToCyrillic(exp, srcLang);
            final Page<String> suggestions = expressionServiceV2.searchSuggestions(checkedExp, srcLang, pageable);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            log.error("Error occurred while retrieving search suggestions: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Page.empty());
        }
    }
}
