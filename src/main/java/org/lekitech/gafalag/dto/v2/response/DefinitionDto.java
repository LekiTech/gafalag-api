package org.lekitech.gafalag.dto.v2.response;

import java.util.List;

/**
 * Represents a single definition with its value and associated tags.
 */
public record DefinitionDto(
        String value,
        List<String> tags
) { }
