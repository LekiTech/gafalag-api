package org.lekitech.gafalag.dto.v2.response;

import java.util.List;

/**
 * Represents the details of the dictionary expression, including inflection and definition details.
 */
public record DetailDto(
        String inflection,
        List<DefinitionDetailDto> definitionDetails,
        List<ExampleDto> examples
) { }
