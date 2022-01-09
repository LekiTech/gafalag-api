package org.lekitech.gafalag.dto.definition;

import org.lekitech.gafalag.entity.Definition;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Mapper
public interface DefinitionMapper {
    DefinitionMapper INSTANCE = Mappers.getMapper(DefinitionMapper.class);


    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface ToBasicResponse {
    }

    @ToBasicResponse
    @Mapping(source = "language.iso3", target = "language")
    @Mapping(source = "source.name", target = "source")
    DefinitionResponse definitionToResponseDto(Definition definition);
}
