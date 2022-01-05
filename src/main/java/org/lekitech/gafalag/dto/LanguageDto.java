package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.lekitech.gafalag.entity.Language;

public record LanguageDto(
    String name,
    String iso639_2,
    String iso639_3
)
{}
