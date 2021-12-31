package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.lekitech.gafalag.enumeration.Gender;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "expression")
public class ExpressionDto {

    private String spelling;
    private String inflection;
    private Boolean misspelling;
    private Gender gender;
    private Long dialectId;
    private Long languageId;
    private Long genderId;
}
