package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.UUID;

/**
 * @param id
 * @param raw
 * @param src
 * @param trl
 * @param tags
 */
public record ExampleDto(
        UUID id,
        String raw,
        String src,
        String trl,
        List<String> tags
) {
}
