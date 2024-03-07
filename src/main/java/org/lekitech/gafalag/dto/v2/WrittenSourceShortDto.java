package org.lekitech.gafalag.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WrittenSourceShortDto {
    /**
     * ID of written source e.g. parsed dictionary book
     */
    UUID id;

    // TODO: rename `name` to `title`
    /**
     * Title of the written source e.g. parsed dictionary book
     */
    String title;

    /**
     * Authors of the written source e.g. parsed dictionary book
     */
    String authors;
}
