package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.Optional;

/**
 * @param definitions {@link DefinitionDto}
 * @param examples    {@link ExampleDto}
 * @param tags        applicable to all `definitions` and `examples`.
 */
public record DefinitionDetailsDto(
        List<DefinitionDto> definitions,
        Optional<List<ExampleDto>> examples,
        Optional<List<String>> tags
) {
}
