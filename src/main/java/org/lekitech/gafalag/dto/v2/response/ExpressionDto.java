package org.lekitech.gafalag.dto.v2.response;

import java.util.List;

/**
 * Represents the root of the dictionary expression with spelling and details.
 */
public record ExpressionDto(
        String spelling,
        List<DetailDto> details
) {}
