package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.DefinitionDto;
import org.lekitech.gafalag.entity.v2.Definition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TagsMapper.class)
interface DefinitionMapperV2 {

    @Mapping(source = "definition.definitionTags", target = "tags")
    DefinitionDto mapToDto(Definition definition);
}
