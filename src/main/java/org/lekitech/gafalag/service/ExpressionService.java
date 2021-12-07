package org.lekitech.gafalag.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ExpressionService {

    @Autowired
    private final ExpressionRepository expressionRepository;

    public Expression saveExp(Expression expression) {
        return expressionRepository.save(expression);
    }
}
