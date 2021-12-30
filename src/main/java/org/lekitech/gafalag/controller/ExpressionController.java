package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.entity.*;
import org.lekitech.gafalag.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/expression")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionController {

    private final ExpressionService expressionService;

    @PostMapping(path = "/batch")
    public List<Expression> saveExpressions(@RequestBody ExpressionBatchRequest request) {
        return expressionService.saveBatch(request);
    }

    @GetMapping(path = "")
    public PaginatedResult<Expression> getExpressionsPaginated(
            @RequestParam String languageIso3,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Boolean> descending) {
        if (sortBy.isPresent() && descending.isPresent()) {
            return expressionService.getPaginated(page, size, sortBy.get(), descending.get());
        } else if (sortBy.isPresent()) {
            return expressionService.getPaginated(page, size, sortBy.get());
        } else {
            return expressionService.getPaginated(page, size);
        }
    }

}
