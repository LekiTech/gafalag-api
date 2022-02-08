package org.lekitech.gafalag.dto.dialect;

import org.lekitech.gafalag.dto.language.LanguageMapper;
import org.lekitech.gafalag.entity.Dialect;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LanguageMapper.class})
public interface DialectMapper {

    @Mapping(source = "languageId", target = "language", qualifiedBy = LanguageMapper.MapByString.class)
    Dialect toEntity(DialectRequest dto);

    @Mapping(source = "language.id", target = "languageId")
    DialectResponse toDto(Dialect entity);
}
