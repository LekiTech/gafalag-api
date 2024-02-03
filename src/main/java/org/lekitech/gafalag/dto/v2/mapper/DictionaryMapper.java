package org.lekitech.gafalag.dto.v2.mapper;

import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.dto.v2.SimilarDto;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = ExpressionDetailsMapper.class)
public interface DictionaryMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "spelling", target = "spelling")
    @Mapping(source = "expressionDetails", target = "details")
    ExpressionResponseDto toDto(UUID id, String spelling, List<ExpressionDetails> expressionDetails);

    List<SimilarDto> toDto(List<Expression> expressions);
}
