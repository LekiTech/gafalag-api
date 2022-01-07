package org.lekitech.gafalag.dto;

public record LanguageRequest(
        String name,
        String iso639_2,
        String iso639_3
) {}
