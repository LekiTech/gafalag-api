package org.lekitech.gafalag.dto.definition;

import java.util.UUID;

public record DefinitionResponse(
        String text,
        String languageIso3,
        UUID sourceId
) {}
