package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.*;
import org.lekitech.gafalag.entity.v2.*;
import org.lekitech.gafalag.projection.DefinitionProjection;
import org.lekitech.gafalag.projection.ExampleProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = ExpressionDetailsMapper.class)
public interface DictionaryMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "spelling", target = "spelling")
    @Mapping(source = "expressionDetails", target = "details")
    ExpressionResponseDto toDto(UUID id, String spelling, List<ExpressionDetails> expressionDetails);

    List<SimilarDto> toDto(List<Expression> expressions);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "raw", target = "raw")
    @Mapping(source = "source", target = "src")
    @Mapping(source = "translation", target = "trl")
    ExampleDto toDto(ExampleProjection example);

    @Mapping(source = "definitionId", target = "id")
    @Mapping(source = "definitionValue", target = "value")
    @Mapping(source = "definitionTags", target = "tags")
    DefinitionDto toDto(DefinitionProjection definitionProjection);
}
