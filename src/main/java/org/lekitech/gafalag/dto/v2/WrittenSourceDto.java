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
public class WrittenSourceDto {
    UUID id;

    String title;

    String authors;

    String publicationYear;

    String providedBy;

    String providedByUrl;

    String processedBy;

    String copyright;

    String seeSourceUrl;

    String description;
}
