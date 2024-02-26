package org.lekitech.gafalag.projection;

import java.util.List;
import java.util.UUID;

public interface ExampleProjection {

    UUID getExpressionId();

    String getExpressionSpelling();

    UUID getExampleId();

    String getSource();

    String getTranslation();

    String getSrcLangId();

    String getTrlLangId();

    String getRaw();

    List<String> getExampleTags();
}
