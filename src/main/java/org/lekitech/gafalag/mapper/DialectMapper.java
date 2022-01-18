package org.lekitech.gafalag.mapper;

import org.lekitech.gafalag.dto.language.DialectRequest;
import org.lekitech.gafalag.dto.language.DialectResponse;
import org.lekitech.gafalag.entity.Dialect;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {
                LanguageMapper.class
        })
public interface DialectMapper {

    @Mappings({
            @Mapping(source = "languageId", target = "language"),
            @Mapping(source = "name", target = "name")
    })
    Dialect toEntity(DialectRequest dto);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "language.id", target = "languageId")
    })
    DialectResponse toDto(Dialect entity);
}
