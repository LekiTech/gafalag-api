package org.lekitech.gafalag.dto;

import java.util.List;
import java.util.Optional;

/*
{
    source: {
        name: string,
        url?: string,
        expressionLanguageIso3: string, // Expression lang ISO-3 e.g. Lezgi
        definitionLanguageIso3: string, // Definition lang ISO-3 e.g. Russian
    },
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
public class ExpressionBatchRequest {
    public record Source(String name, Optional<String> url, String expressionLanguageIso3, String definitionLanguageIso3){}
    public Source source;

    public record Article(String spelling, Optional<String> inflection, List<String> definitions){}
    public List<Article> dictionary;
}
