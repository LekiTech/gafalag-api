package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param spelling
 * @param details  {@link ExpressionDetailsDto}
 */
public record ExpressionResponseDto(
        String spelling,
        List<ExpressionDetailsDto> details
) {
}
