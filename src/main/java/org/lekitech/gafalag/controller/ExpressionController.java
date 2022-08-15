package org.lekitech.gafalag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.PaginatedResult;
import org.lekitech.gafalag.dto.expression.*;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/expression")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionController {

    private final ExpressionService expressionService;
    private final ExpressionMapper expressionMapper;

    @PostMapping(path = "/batch", consumes = "multipart/form-data")
    public void saveExpressions(@RequestPart("file") MultipartFile file) {
        try {
            val mapper = new ObjectMapper().registerModule(new Jdk8Module());
            expressionService.saveBatch(mapper.readValue(
                    file.getBytes(), ExpressionBatchRequest.class
            ));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public void saveExpression(@RequestBody ExpressionRequest request) {
        expressionService.save(request);
    }

    @Transactional
    @GetMapping(path = "/search")
    public List<ExpressionResponse> search(@RequestParam String exp,
                                           @RequestParam String fromLang,
                                           @RequestParam String toLang) {
        return expressionService.fuzzySearch(exp, fromLang, toLang).stream().map(expressionMapper::toDto).toList();
    }

    @Transactional
    @GetMapping(path = "/search/suggestions")
    public List<String> searchSuggestions(@RequestParam String exp,
                                          @RequestParam String fromLang,
                                          @RequestParam String toLang) {
        return expressionService.fuzzySearch(exp, fromLang, toLang).stream().map(Expression::getSpelling).toList();
    }

    @Transactional
    @GetMapping(path = "/search/definition")
    public List<ExpressionResponse> searchByDefinition(@RequestParam String text,
                                                       @RequestParam String fromLang,
                                                       @RequestParam String toLang) {
        return expressionService.fullTextSearch(text, fromLang, toLang).stream().map(expressionMapper::toDto).toList();
    }

    @Transactional
    @GetMapping(path = "/search/suggestions")
    public List<String> searchSuggestions(@RequestParam String exp) {
        return expressionService.fuzzySearch(exp).stream().map(Expression::getSpelling).toList();
    }

    @GetMapping(path = "")
    public PaginatedResult<ExpressionResponse> getExpressionsPaginated(
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
