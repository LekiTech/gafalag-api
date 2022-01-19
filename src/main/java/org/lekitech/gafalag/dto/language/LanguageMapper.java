package org.lekitech.gafalag.dto.language;

import org.lekitech.gafalag.dto.mapper.ReferenceMapper;
import org.lekitech.gafalag.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.*;

@Mapper(componentModel = "spring", uses = ReferenceMapper.class)
public interface LanguageMapper {

    @MapByDto
    Language toEntity(LanguageRequest dto);

    @MapByEntity
    LanguageResponse toDto(Language entity);

    @MapByLong
    Language map(Long id);

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface MapByDto {}

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface MapByEntity {}

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface MapByLong {}
}
