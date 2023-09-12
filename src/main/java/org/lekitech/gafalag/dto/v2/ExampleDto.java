package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.Optional;

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
        Optional<List<String>> tags
) {}
