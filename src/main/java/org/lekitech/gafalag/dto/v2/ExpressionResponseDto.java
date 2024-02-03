package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.UUID;

/**
 * @param id
 * @param spelling
 * @param details  {@link ExpressionDetailsDto}
 */
public record ExpressionResponseDto(
        UUID id,
        String spelling,
        List<ExpressionDetailsDto> details
) {
}
