package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param spelling
 * @param details  {@link ExpressionDetailsDto}
 */
public record ExpressionDto(
        List<String> spelling,
        List<ExpressionDetailsDto> details
) {
}
