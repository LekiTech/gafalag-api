package org.lekitech.gafalag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/expression")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionController {

    private final ExpressionService expressionService;

    @PostMapping(path = "/batch")
    public HttpStatus saveExpressions(@RequestParam("file") @NonNull MultipartFile file) {
        if (file.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        try {
            expressionService.saveBatch(new ObjectMapper().readValue(
                    file.getBytes(), ExpressionBatchRequest.class
            ));
            return HttpStatus.CREATED;
        } catch (IOException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping
    public HttpStatus saveExpression(@RequestBody ExpressionRequest expression) {
        expressionService.save(expression);
        return HttpStatus.CREATED;
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
