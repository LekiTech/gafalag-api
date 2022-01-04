package org.lekitech.gafalag.dto;

import org.lekitech.gafalag.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public record ExpressionRequest(
        UUID sourceId,
        String expressionLanguageIso3,
        String definitionLanguageIso3,
        String spelling,
        Optional<String> inflection,
        List<String> definitions
) {

    public Expression content(Source source,
                              Language expressionLanguage,
                              Language definitionLanguage) {
        return new Expression(
                spelling,
                inflection,
                expressionLanguage,
                definitions(source, definitionLanguage)
        );
    }

    public List<Definition> definitions(Source source,
                                        Language language) {
        return definitions.stream()
                .map(definition -> new Definition(
                        definition,
                        language,
                        source
                )).collect(Collectors.toList());
    }
}