package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param name                 source dictionary
 * @param authors
 * @param publicationYear
 * @param description
 * @param providedBy
 * @param providedByURL        source dictionary
 * @param processedBy
 * @param copyright
 * @param seeSourceURL
 * @param expressionLanguageId expression lang ISO-3 e.g. 'lez' - for Lezgi
 * @param definitionLanguageId definitions lang ISO-3 e.g. 'rus' - for Russian
 * @param expressions          content {@link ExpressionDto}
 */
public record DictionaryDto(
        String name,
        String authors,
        String publicationYear,
        String description,
        String providedBy,
        String providedByURL,
        String processedBy,
        String copyright,
        String seeSourceURL,
        String expressionLanguageId,
        String definitionLanguageId,
        List<ExpressionDto> expressions
) {
}
