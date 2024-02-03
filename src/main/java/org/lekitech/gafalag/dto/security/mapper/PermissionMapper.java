package org.lekitech.gafalag.dto.security.mapper;

import org.lekitech.gafalag.dto.security.PermissionDto;
import org.lekitech.gafalag.entity.v2.security.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The `PermissionMapper` interface defines mapping methods to convert `Permission` entities to
 * `PermissionDto` data transfer objects and vice versa. It utilizes MapStruct for automatic
 * mapping configuration.
 * <p>
 * This mapper is responsible for transforming permission-related data between entity and DTO
 * representations.
 */
@Mapper(componentModel = "spring")
interface PermissionMapper {

    /**
     * Converts a `Permission` entity to a `PermissionDto` data transfer object.
     *
     * @param permission The `Permission` entity to be converted.
     * @return A `PermissionDto` representing the converted entity.
     */
    @Mapping(source = "permission.language.id", target = "language")
    PermissionDto toDto(Permission permission);
}
