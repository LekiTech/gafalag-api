package org.lekitech.gafalag.dto.expression;

import java.util.*;

public record ExpressionRequest(
        UUID sourceId,
        String expressionLanguageIso3,
        String definitionLanguageIso3,
        String spelling,
        Optional<String> inflection,
        List<String> definitions
) {}
