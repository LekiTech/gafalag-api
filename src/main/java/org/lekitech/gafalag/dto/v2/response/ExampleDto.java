package org.lekitech.gafalag.dto.v2.response;

import java.util.List;

/**
 * Represents an example with source, translation, raw text, and tags.
 */
public record ExampleDto(
        String src,
        String trl,
        String raw,
        List<String> tags
) { }
