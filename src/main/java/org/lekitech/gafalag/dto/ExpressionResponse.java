package org.lekitech.gafalag.dto;

import java.util.List;
import java.util.UUID;

public record ExpressionResponse(
        UUID id,
        String spelling,
        Boolean misspelling,
        String inflection,
        Long genderId,
        Long languageId,
        Long dialectId,
        List<DefinitionResponse> definitions
) {}
