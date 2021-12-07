package org.lekitech.gafalag.controller;

import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/gafalag")
public class GafalagApi {

    @Autowired
    private ExpressionService expressionService;

    @PostMapping("/expression")
    public ResponseEntity<Expression> save(@RequestParam("exp") String exp) {
        return new ResponseEntity<>(
                expressionService.saveExp(new Expression()),
                HttpStatus.OK
        );
    }
}
