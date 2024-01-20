package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param value
 * @param tags  applicable to all `definitions` and `examples`.
 */
public record DefinitionDto(
        String value,
        List<String> tags
) {
}
