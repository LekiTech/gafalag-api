package org.lekitech.gafalag.dto.v2;

import java.util.List;

/**
 * @param definitions {@link Definition}
 * @param examples    {@link Example}
 * @param tags        applicable to all `definitions` and `examples`.
 */
public record DefinitionDetails(
        List<Definition> definitions,
        List<Example> examples,
        List<String> tags
) {}
