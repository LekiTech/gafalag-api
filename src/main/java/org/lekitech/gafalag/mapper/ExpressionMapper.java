package org.lekitech.gafalag.mapper;

import org.lekitech.gafalag.dto.expression.ExpressionResponse;
import org.lekitech.gafalag.entity.Expression;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                DefinitionMapper.class
        })
public interface ExpressionMapper {

    ExpressionResponse toDto(Expression expression);
}
