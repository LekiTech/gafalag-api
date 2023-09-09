package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.Optional;

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
 * @param expressions          content {@link Expression}
 */
public record Dictionary(
        String name,
        Optional<String> authors,
        Optional<String> publicationYear,
        Optional<String> description,
        Optional<String> providedBy,
        Optional<String> providedByURL,
        Optional<String> processedBy,
        Optional<String> copyright,
        Optional<String> seeSourceURL,
        String expressionLanguageId,
        String definitionLanguageId,
        List<Expression> expressions
) {}
