package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.lekitech.gafalag.entity.Definition;
import org.lekitech.gafalag.entity.Expression;
import org.lekitech.gafalag.enumeration.Gender;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "expression")
public class ExpressionDto {
    public UUID id;
    private String spelling;
    private Boolean misspelling;
    private String inflection;
    private Long genderId;
    private Long languageId;
    private Long dialectId;
//    TODO: create DefinitionDto and use here
    private List<String> definitions;

    public ExpressionDto(Expression expression) {
        this.id = expression.id;
        this.spelling = expression.spelling;
        this.misspelling = expression.misspelling;
        this.inflection = expression.inflection;
        this.genderId = expression.genderId;
        this.languageId = expression.languageId;
        this.dialectId = expression.dialectId;
        this.definitions = expression.definitions.stream().map(def -> def.text).toList();
    }
}
