package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.lekitech.gafalag.enumeration.Gender;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@JsonRootName(value = "response")
public class Response {

    private String spelling;
    private Boolean misspelling;
    private String inflection;
    private Gender gender;
    private LanguageDto language;
    private String dialect;
}