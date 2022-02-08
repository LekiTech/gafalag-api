package org.lekitech.gafalag.dto.mapper;

import org.lekitech.gafalag.entity.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.*;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = ReferenceMapper.class)
public interface SourceMapper {

    @MapByUUID
    Source map(UUID id);

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface MapByUUID {}
}
