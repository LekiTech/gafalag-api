package org.lekitech.gafalag.dto.v2;

import java.util.List;

public record ExpressionAndSimilar(
        ExpressionResponseDto found,
        List<SimilarDto> similar
) {
}
