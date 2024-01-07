package org.lekitech.gafalag.service.v2;

import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.ExpressionDto;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExpressionServiceV2 {

    private final ExpressionRepositoryV2 expressionRepositoryV2;

    public ExpressionServiceV2(ExpressionRepositoryV2 expressionRepositoryV2) {
        this.expressionRepositoryV2 = expressionRepositoryV2;
    }

    public Expression save(ExpressionDto dto) {

        return expressionRepositoryV2.save(new Expression());
    }
}
