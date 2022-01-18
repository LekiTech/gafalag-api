package org.lekitech.gafalag.dto.language;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LanguageRequest(
        String name,
        @JsonProperty("iso639_2")
        String iso2,
        @JsonProperty("iso639_3")
        String iso3
) {}
