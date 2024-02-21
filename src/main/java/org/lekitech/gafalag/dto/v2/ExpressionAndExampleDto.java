package org.lekitech.gafalag.dto.v2;

    import java.util.UUID;

public record ExpressionAndExampleDto(
        UUID id,
        String spelling,
        ExampleDto example
) {
}
