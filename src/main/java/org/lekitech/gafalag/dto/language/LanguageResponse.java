package org.lekitech.gafalag.dto.language;

public record LanguageResponse(
        Long id,
        String name,
        String iso2,
        String iso3
) {}
