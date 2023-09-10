package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param definitions {@link DefinitionDto}
 * @param examples    {@link ExampleDto}
 * @param tags        applicable to all `definitions` and `examples`.
 */
public record DefinitionDetailsDto(
        List<DefinitionDto> definitions,
        List<ExampleDto> examples,
        List<String> tags
) {}
