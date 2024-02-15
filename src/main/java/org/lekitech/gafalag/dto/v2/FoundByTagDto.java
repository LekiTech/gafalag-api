package org.lekitech.gafalag.dto.v2;

import java.util.List;

public record FoundByTagDto(
        String id,
        String spelling,
        String expLangId,
        ShortDefinitionDto definition,
        ShortExampleDto example
) {

    public record ShortDefinitionDto(
            String value,
            List<String> tags
    ) {
    }

    public record ShortExampleDto(
            String source,
            String translation,
            List<String> tags
    ) {
    }
}
