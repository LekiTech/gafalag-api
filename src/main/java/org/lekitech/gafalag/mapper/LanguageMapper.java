package org.lekitech.gafalag.mapper;

import org.lekitech.gafalag.dto.language.LanguageRequest;
import org.lekitech.gafalag.dto.language.LanguageResponse;
import org.lekitech.gafalag.entity.Language;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                ReferenceMapper.class
        })
public interface LanguageMapper {

    Language toEntity(LanguageRequest dto);

    Language map(Long id);

    LanguageResponse toDto(Language entity);
}
