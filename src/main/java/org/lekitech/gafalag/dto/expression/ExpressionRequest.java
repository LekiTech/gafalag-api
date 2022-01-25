package org.lekitech.gafalag.dto.expression;

import java.util.*;

public record ExpressionRequest(
        UUID sourceId,
        String expressionLanguageId,
        String definitionLanguageId,
        String spelling,
        Optional<String> inflection,
        List<String> definitions
) {}
