package org.lekitech.gafalag.dto.definition;

import org.lekitech.gafalag.entity.Definition;
import org.mapstruct.*;

import java.lang.annotation.*;

@Mapper(componentModel = "spring")
public interface DefinitionMapper {

    @Mappings({
            @Mapping(source = "language.iso3", target = "languageIso3"),
            @Mapping(source = "source.id", target = "sourceId")
    })
    @MapByEntity
    DefinitionResponse toDto(Definition definition);

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface MapByEntity {}
}
