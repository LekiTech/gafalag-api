package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param spelling
 * @param details  {@link ExpressionDetails}
 */
public record Expression(
        String spelling,
        List<ExpressionDetails> details
) {}
