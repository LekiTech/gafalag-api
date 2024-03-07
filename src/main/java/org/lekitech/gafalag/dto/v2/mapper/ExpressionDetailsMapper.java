package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.ExpressionDetailsDto;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ExpressionExampleMapper.class,
        ExpressionDefinitionDetailsMapper.class,
        SourceMapperV2.class
})
public interface ExpressionDetailsMapper {

    @Mapping(source = "expressionExamples", target = "examples")
    @Mapping(source = "source.writtenSources", target = "writtenSources")
    ExpressionDetailsDto mapToDto(ExpressionDetails expressionDetails);
}
