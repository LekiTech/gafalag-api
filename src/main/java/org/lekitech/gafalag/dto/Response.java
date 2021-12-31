package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@JsonRootName(value = "response")
public class Response {

    private String spelling;
    private Boolean misspelling;
    private String inflection;
    private String gender;
    private LanguageDto language;
    private String dialect;
}