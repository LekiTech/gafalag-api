package org.lekitech.gafalag.dto.definition;

public record DefinitionResponse(
        String text,
        String language,
        String source
) {}
