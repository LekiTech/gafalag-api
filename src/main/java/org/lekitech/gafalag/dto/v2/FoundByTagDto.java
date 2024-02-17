package org.lekitech.gafalag.dto.v2;

import org.lekitech.gafalag.entity.v2.Expression;

import java.util.List;

public record FoundByTagDto(
        String id,
        String spelling,
        String expLangId,
        ShortDefinitionDto definition,
        ShortExampleDto example
) {

    public FoundByTagDto(Expression expression, ShortExampleDto example) {
        this(expression.getId().toString(),
                expression.getSpelling(),
                expression.getLanguage().getId(),
                null,
                example
        );
    }

    public FoundByTagDto(Expression expression, ShortDefinitionDto definition) {
        this(expression.getId().toString(),
                expression.getSpelling(),
                expression.getLanguage().getId(),
                definition,
                null
        );
    }

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
