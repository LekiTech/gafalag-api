package org.lekitech.gafalag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public HttpStatus saveExpressions(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        val mapper = new ObjectMapper().registerModule(new Jdk8Module());
        try {
            expressionService.saveBatch(mapper.readValue(
                    file.getBytes(), ExpressionBatchRequest.class
            ));
            return HttpStatus.CREATED;
        } catch (IOException e) {
            log.error(e.getMessage());
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
