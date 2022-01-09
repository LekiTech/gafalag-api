package org.lekitech.gafalag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.dto.expression.ExpressionBatchRequest;
import org.lekitech.gafalag.dto.expression.ExpressionRequest;
import org.lekitech.gafalag.dto.expression.ExpressionResponse;
import org.lekitech.gafalag.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/expression")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionController {

    private final ExpressionService expressionService;

    @PostMapping(path = "/batch", consumes = "multipart/form-data")
    public ResponseEntity<String> saveExpressions(@RequestPart("file") MultipartFile file) {
        try {
            val mapper = new ObjectMapper().registerModule(new Jdk8Module());
            expressionService.saveBatch(mapper.readValue(
                    file.getBytes(), ExpressionBatchRequest.class
            ));
            return ResponseEntity.ok().body(MessageFormat.format("{0} - uploaded success!", file.getOriginalFilename()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PostMapping
    public HttpStatus saveExpression(@RequestBody ExpressionRequest expression) {
        expressionService.save(expression);
        return HttpStatus.CREATED;
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
