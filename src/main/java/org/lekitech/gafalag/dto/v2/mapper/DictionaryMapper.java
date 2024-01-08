package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {

    @Mapping(source = "spelling", target = "spelling")
    ExpressionResponseDto toDto(String spelling, List<ExpressionDetails> expressionDetails);
}
