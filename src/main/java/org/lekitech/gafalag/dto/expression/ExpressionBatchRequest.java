package org.lekitech.gafalag.dto.expression;

import java.util.List;
import java.util.Optional;

/**
 * @param name                 source dictionary
 * @param url                  source dictionary
 * @param expressionLanguageId expression lang ISO-3 e.g. 'lez' - for Lezgian
 * @param definitionLanguageId definitions lang ISO-3 e.g. 'rus' - for Russian
 * @param dictionary           content {@link Article}
 */
public record ExpressionBatchRequest(
        String name,
        Optional<String> url,
        String expressionLanguageId,
        String definitionLanguageId,
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
    ) {
    }
}
