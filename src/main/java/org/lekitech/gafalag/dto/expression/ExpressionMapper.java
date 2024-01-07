package org.lekitech.gafalag.dto.expression;

import org.lekitech.gafalag.dto.PaginatedResult;
import org.lekitech.gafalag.dto.definition.DefinitionMapper;
import org.lekitech.gafalag.entity.v1.Expression;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {DefinitionMapper.class})
public interface ExpressionMapper {

    default PaginatedResult<ExpressionResponse> toDto(Page<Expression> page) {
        return new PaginatedResult<>(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.getContent().stream().map(this::toDto).toList()
        );
    }

    @Mapping(source = "gender.id", target = "genderId")
    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source = "dialect.id", target = "dialectId")
    @Mapping(source = "definitions", target = "definitions", qualifiedBy = DefinitionMapper.MapByEntity.class)
    ExpressionResponse toDto(Expression expression);
}
