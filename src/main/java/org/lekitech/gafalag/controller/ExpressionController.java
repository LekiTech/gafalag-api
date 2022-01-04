package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/expression")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionController {

    private final ExpressionService expressionService;
    private final LanguageService languageService;
    private final SourceService sourceService;

    @PostMapping(path = "/batch")
    public List<Expression> saveExpression(@RequestBody ExpressionBatchRequest request) {
        return expressionService.saveBatch(request);
    }

    @PostMapping
    public HttpStatus saveExpression(@RequestBody ExpressionRequest expression) {
        val source = sourceService.getById(expression.sourceId());
        val expressionLang = languageService.getByIso3(expression.expressionLanguageIso3());
        val definitionLang = languageService.getByIso3(expression.definitionLanguageIso3());
        expressionService.save(expression.content(source, expressionLang, definitionLang));
        return HttpStatus.OK;
    }

    @GetMapping(path = "")
    public PaginatedResult<ExpressionDto> getExpressionsPaginated(
            @RequestParam int page,
            @RequestParam int size,
            Optional<String> lang,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Boolean> descending) {
        if (sortBy.isPresent() && descending.isPresent()) {
            return expressionService.getPaginated(page, size, lang, sortBy.get(), descending.get());
        } else if (sortBy.isPresent()) {
            return expressionService.getPaginated(page, size, lang, sortBy.get());
        } else {
            return expressionService.getPaginated(page, size, lang);
        }
    }

}
