package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.lekitech.gafalag.entity.Language;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonRootName(value = "language")
public class LanguageDto {

    private String name;
    private String iso639_2;
    private String iso639_3;

    public LanguageDto(@NonNull Language language) {
        this.name = language.getName();
        this.iso639_2 = language.getIso639_2();
        this.iso639_3 = language.getIso639_3();
    }
}
