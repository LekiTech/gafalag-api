package org.lekitech.gafalag.dto.v2;

import java.util.List;

public record ExpressionAndSuggestions(
        ExpressionResponseDto expression,
        List<String> suggestions
) {
}
