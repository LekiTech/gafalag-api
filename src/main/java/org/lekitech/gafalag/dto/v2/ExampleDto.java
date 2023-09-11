package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param raw
 * @param src
 * @param trl
 * @param tags
 */
public record ExampleDto(
        String raw,
        String src,
        String trl,
        List<String> tags
) {}
