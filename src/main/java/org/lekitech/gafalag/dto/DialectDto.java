package org.lekitech.gafalag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonRootName(value = "dialect")
public class DialectDto {

    private String name;
    private Long languageId;
}
