package org.lekitech.gafalag.enumeration;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Gender {

    FEMININE,
    MASCULINE,
    NEUTER,
    @JsonEnumDefaultValue UNDEFINED
}
