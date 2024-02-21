package org.lekitech.gafalag.dto.v2;

import java.util.List;

public record ExpressionAndSimilarDto(
        ExpressionResponseDto found,
        List<SimilarDto> similar
) {
}
