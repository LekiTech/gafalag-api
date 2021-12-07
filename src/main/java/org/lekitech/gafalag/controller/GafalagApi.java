package org.lekitech.gafalag.controller;

import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.ExpressionDto;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.service.DialectService;
import org.lekitech.gafalag.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/gafalag")
public class GafalagApi {

    @Autowired
    private ExpressionService expressionService;
    @Autowired
    private DialectService dialectService;

    @PostMapping("/expression")
    public ResponseEntity<Expression> save(
            @RequestBody ExpressionDto expressionDto
    ) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
