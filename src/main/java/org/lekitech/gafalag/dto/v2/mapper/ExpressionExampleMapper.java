package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.ExampleDto;
import org.lekitech.gafalag.entity.v2.ExpressionExample;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TagsMapper.class)
interface ExpressionExampleMapper {

    @Mapping(source = "expressionExample.example.raw", target = "raw")
    @Mapping(source = "expressionExample.example.srcLanguage.id", target = "src")
    @Mapping(source = "expressionExample.example.translation", target = "trl")
    @Mapping(source = "expressionExample.example.exampleTags", target = "tags")
    ExampleDto mapToDto(ExpressionExample expressionExample);
}
