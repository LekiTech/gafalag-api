package org.lekitech.gafalag.projection;

import java.util.UUID;

public interface ExampleProjection {

    UUID getExpressionId();

    String getExpressionSpelling();

    UUID getId();

    String getSource();

    String getTranslation();

    String getSrcLangId();

    String getTrlLangId();

    String getRaw();
}
