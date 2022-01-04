package org.lekitech.gafalag.dto;

import org.lekitech.gafalag.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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