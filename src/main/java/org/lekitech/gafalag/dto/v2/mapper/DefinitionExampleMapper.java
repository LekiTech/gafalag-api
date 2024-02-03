package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.ExampleDto;
import org.lekitech.gafalag.entity.v2.DefinitionExample;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TagsMapper.class)
interface DefinitionExampleMapper {

    @Mapping(source = "definitionExample.example.id", target = "id")
    @Mapping(source = "definitionExample.example.raw", target = "raw")
    @Mapping(source = "definitionExample.example.srcLanguage.id", target = "src")
    @Mapping(source = "definitionExample.example.translation", target = "trl")
    @Mapping(source = "definitionExample.example.exampleTags", target = "tags")
    ExampleDto mapToDto(DefinitionExample definitionExample);
}
