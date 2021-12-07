package org.lekitech.gafalag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpressionDto {

    private String spelling;
    private Boolean misspelling;
    private String inflection;
    private Long genderId;
    private Long languageId;
    private Long dialectId;
}
