package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.entity.*;
import org.lekitech.gafalag.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
