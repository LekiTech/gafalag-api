package org.lekitech.gafalag.mapper;

import org.lekitech.gafalag.dto.definition.DefinitionResponse;
import org.lekitech.gafalag.entity.Definition;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DefinitionMapper {

    @Mappings({
            @Mapping(source = "text", target = "text"),
            @Mapping(source = "language.iso3", target = "languageIso3"),
            @Mapping(source = "source.id", target = "sourceId")
    })
    DefinitionResponse toDto(Definition definition);
}
