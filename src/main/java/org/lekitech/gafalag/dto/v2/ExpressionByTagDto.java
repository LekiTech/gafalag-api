package org.lekitech.gafalag.dto.v2;

import org.lekitech.gafalag.entity.v2.DefinitionExample;
import org.lekitech.gafalag.entity.v2.Example;
import org.lekitech.gafalag.entity.v2.Expression;

import java.util.List;

public record ExpressionByTagDto(
        String id,
        String spelling,
        String expLangId,
        ShortDefinitionDto definition,
        ShortExampleDto example
) {

    public ExpressionByTagDto(Expression expression, ShortExampleDto example) {
        this(expression.getId().toString(),
                expression.getSpelling(),
                expression.getLanguage().getId(),
                null,
                example
        );
    }

    public ExpressionByTagDto(Expression expression, ShortDefinitionDto definition) {
        this(expression.getId().toString(),
                expression.getSpelling(),
                expression.getLanguage().getId(),
                definition,
                null
        );
    }

    public record ShortDefinitionDto(String value, List<String> tags) {
    }

    public record ShortExampleDto(String source, String translation, List<String> tags) {

        public ShortExampleDto(Example example, List<String> tags) {
            this(example.getSource(),
                    example.getTranslation(),
                    tags);
        }
    }

}
