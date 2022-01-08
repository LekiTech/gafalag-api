package org.lekitech.gafalag.dto.expression;

import org.lekitech.gafalag.dto.definition.DefinitionMapper;
import org.lekitech.gafalag.entity.Expression;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { DefinitionMapper.class })
public interface ExpressionMapper {
    ExpressionMapper INSTANCE = Mappers.getMapper(ExpressionMapper.class);

    @Mapping(source = "definitions", target = "definitions", qualifiedBy = DefinitionMapper.ToBasicResponse.class)
    ExpressionResponse expressionToResponseDto(Expression expression);

}
