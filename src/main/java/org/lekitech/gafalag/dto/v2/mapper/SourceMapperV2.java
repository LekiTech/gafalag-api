package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.DefinitionDto;
import org.lekitech.gafalag.dto.v2.WrittenSourceDto;
import org.lekitech.gafalag.dto.v2.WrittenSourceShortDto;
import org.lekitech.gafalag.entity.v2.Definition;
import org.lekitech.gafalag.entity.v2.WrittenSource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface SourceMapperV2 {

    @Mapping(source = "name", target = "title")
    WrittenSourceDto mapToDto(WrittenSource writtenSource);

    @Mapping(source = "name", target = "title")
    WrittenSourceShortDto mapToShortDto(WrittenSource writtenSource);
}
