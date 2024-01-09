package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.entity.v2.DefinitionDetailsTag;
import org.lekitech.gafalag.entity.v2.DefinitionTag;
import org.lekitech.gafalag.entity.v2.ExampleTag;
import org.lekitech.gafalag.entity.v2.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TagsMapper {

    default String mapToDto(Tag tag) {
        return tag != null
                ? tag.getAbbreviation()
                : null;
    }

    default String mapToDto(DefinitionDetailsTag definitionDetailsTag) {
        return (definitionDetailsTag != null && definitionDetailsTag.getTag() != null)
                ? mapToDto(definitionDetailsTag.getTag())
                : null;
    }

    default String mapToDto(DefinitionTag definitionTag) {
        return (definitionTag != null && definitionTag.getTag() != null)
                ? mapToDto(definitionTag.getTag())
                : null;
    }

    default String mapToDto(ExampleTag exampleTag) {
        return (exampleTag != null && exampleTag.getTag() != null)
                ? mapToDto(exampleTag.getTag())
                : null;
    }

}
