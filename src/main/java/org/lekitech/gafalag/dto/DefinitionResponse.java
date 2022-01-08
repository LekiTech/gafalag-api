package org.lekitech.gafalag.dto;

public record DefinitionResponse(
        String definition,
        String language,
        String source
) {}
