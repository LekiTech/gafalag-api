package org.lekitech.gafalag.dto.v2;

import java.util.List;
import java.util.UUID;

/**
 * @param id
 * @param definitions {@link DefinitionDto}
 * @param examples    {@link ExampleDto}
 * @param tags        applicable to all `definitions` and `examples`.
 */
public record DefinitionDetailsDto(
        UUID id,
        List<DefinitionDto> definitions,
        List<ExampleDto> examples,
        List<String> tags
) {
}
