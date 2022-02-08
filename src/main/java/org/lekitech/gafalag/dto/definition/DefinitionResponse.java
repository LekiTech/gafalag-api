package org.lekitech.gafalag.dto.definition;

import java.util.UUID;

public record DefinitionResponse(
        String text,
        String languageId,
        UUID sourceId
) {}
