package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.Optional;

/**
 * @param value
 * @param tags  applicable to all `definitions` and `examples`.
 */
public record DefinitionDto(
        String value,
        Optional<List<String>> tags
) {}
