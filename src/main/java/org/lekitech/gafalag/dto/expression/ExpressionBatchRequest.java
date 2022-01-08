package org.lekitech.gafalag.dto.expression;

import java.util.List;
import java.util.Optional;

/**
 * @param name                   source dictionary
 * @param url                    source dictionary
 * @param expressionLanguageIso3 expression lang ISO-3 e.g. 'Lezgi'
 * @param definitionLanguageIso3 definitions lang ISO-3 e.g. 'Russian'
 * @param dictionary             content {@link Article}
 */
public record ExpressionBatchRequest(
        String name,
        Optional<String> url,
        String expressionLanguageIso3,
        String definitionLanguageIso3,
        List<Article> dictionary
) {

    /**
     * @param spelling    source expression
     * @param inflection  possible value
     * @param definitions an array with formatted texts
     */
    public record Article(
            String spelling,
            Optional<String> inflection,
            List<String> definitions
    ) {}
}
