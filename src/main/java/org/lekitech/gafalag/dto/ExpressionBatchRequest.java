package org.lekitech.gafalag.dto;

import java.util.List;
import java.util.Optional;

/*
{
    name: string,
    url?: string,
    expressionLanguageIso3: string, // Expression lang ISO-3 e.g. Lezgi
    definitionLanguageIso3: string, // Definition lang ISO-3 e.g. Russian
    dictionary: [
        {
            spelling: string,
            inflection?: string,
            definitions: string[],
                ...
            ],
        },
        ...
    ]
}
 */
public record ExpressionBatchRequest (
    String name,
    Optional<String> url,
    String expressionLanguageIso3,
    String definitionLanguageIso3,
    List<Article> dictionary
){
    public record Article(String spelling, Optional<String> inflection, List<String> definitions){}
}
