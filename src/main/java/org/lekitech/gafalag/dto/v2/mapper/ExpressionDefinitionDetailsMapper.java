package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.DefinitionDetailsDto;
import org.lekitech.gafalag.entity.v2.DefinitionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        DefinitionExampleMapper.class,
        DefinitionMapperV2.class,
        TagsMapper.class
})
interface ExpressionDefinitionDetailsMapper {

    @Mapping(source = "definitionDetails.definitionExamples", target = "examples")
    @Mapping(source = "definitionDetails.definitionDetailsTags", target = "tags")
    DefinitionDetailsDto mapToDto(DefinitionDetails definitionDetails);
}
