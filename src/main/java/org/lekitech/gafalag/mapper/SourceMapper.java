package org.lekitech.gafalag.mapper;

import org.lekitech.gafalag.entity.Source;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring",
        uses = {
                ReferenceMapper.class
        })
public interface SourceMapper {

    Source map(UUID id);
}
