package org.lekitech.gafalag.dto.expression;

import org.lekitech.gafalag.dto.definition.DefinitionMapper;
import org.lekitech.gafalag.entity.Expression;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DefinitionMapper.class})
public interface ExpressionMapper {

    @Mapping(source = "gender.id", target = "genderId")
    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source = "dialect.id", target = "dialectId")
    @Mapping(source = "definitions", target = "definitions", qualifiedBy = DefinitionMapper.MapByEntity.class)
    ExpressionResponse toDto(Expression expression);
}
