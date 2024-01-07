package org.lekitech.gafalag.dto.v2.response;

import java.util.List;

/**
 * Represents detailed definitions including the definition itself and examples.
 */
public record DefinitionDetailDto(
        List<DefinitionDto> definitions,
        List<ExampleDto> examples,
        List<String> tags
) { }
