package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.UUID;

/**
 * @param id
 * @param value
 * @param tags  applicable to all `definitions` and `examples`.
 */
public record DefinitionDto(
        UUID id,
        String value,
        String defLangId,
        List<String> tags
) {
}
