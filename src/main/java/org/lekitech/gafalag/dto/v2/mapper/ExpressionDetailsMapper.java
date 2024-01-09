package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.ExpressionDetailsDto;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ExpressionExampleMapper.class,
        ExpressionDefinitionDetailsMapper.class
})
public interface ExpressionDetailsMapper {

    @Mapping(source = "expressionExamples", target = "examples")
    ExpressionDetailsDto mapToDto(ExpressionDetails expressionDetails);
}
