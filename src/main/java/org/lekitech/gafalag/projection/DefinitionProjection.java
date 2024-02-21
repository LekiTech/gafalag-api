package org.lekitech.gafalag.projection;

import java.util.List;
import java.util.UUID;

public interface DefinitionProjection {

    UUID getExpressionId();

    String getExpressionSpelling();

    UUID getDefinitionId();

    String getDefinitionValue();

    List<String> getTags();
}
