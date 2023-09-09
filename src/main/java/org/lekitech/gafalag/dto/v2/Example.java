package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.Optional;

/**
 * @param raw
 * @param src
 * @param trl
 * @param tags
 */
public record Example(
        String raw,
        Optional<String> src,
        Optional<String> trl,
        List<String> tags
) {}
