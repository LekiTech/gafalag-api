package org.lekitech.gafalag.dto.v2;

import java.util.UUID;

public record ExpressionAndDefinitionDto(
        UUID id,
        String spelling,
        DefinitionDto definition
) {
}
