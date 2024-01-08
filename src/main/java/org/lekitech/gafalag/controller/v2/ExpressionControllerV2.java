package org.lekitech.gafalag.controller.v2;

// [Other imports...]

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.ExpressionDto;
import org.lekitech.gafalag.service.v2.ExpressionServiceV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v2/expressions")
public class ExpressionControllerV2 {

    private final ExpressionServiceV2 expressionServiceV2;

    @GetMapping("/pages")
    public Page<ExpressionDto> listExpressions(@RequestParam String spelling,
                                               @RequestParam String expLang,
                                               Pageable pageable) {
        try {
            return expressionServiceV2.findExpressionsBySpellingAndLanguageId(spelling, expLang, pageable);
        } catch (Exception e) {
            log.error("Failed to fetch expression pages", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to retrieve expression pages", e);
        }
    }
}
