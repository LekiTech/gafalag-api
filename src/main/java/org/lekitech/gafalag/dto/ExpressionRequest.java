package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.lekitech.gafalag.entity.*;

import java.util.UUID;

public record ExpressionRequest(
        UUID sourceId,
        String expressionLanguageIso3,
        String definitionLanguageIso3,
        @JsonUnwrapped
        Article article
) {

    public Expression content(Source source,
                              Language expressionLanguage,
                              Language definitionLanguage) {
        return new Expression(
                article.spelling(),
                article.inflection(),
                expressionLanguage,
                article.definitions(source, definitionLanguage)
        );
    }
}
