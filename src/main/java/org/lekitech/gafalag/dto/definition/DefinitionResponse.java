package org.lekitech.gafalag.dto.definition;

public record DefinitionResponse(
        String text,
        // TODO: change name to languageIso3
        String language,
        // TODO: change to object or to sourceId
        String source
) {}
