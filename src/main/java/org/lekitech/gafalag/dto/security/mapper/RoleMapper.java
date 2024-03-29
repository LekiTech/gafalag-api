package org.lekitech.gafalag.dto.security.mapper;

import org.lekitech.gafalag.dto.security.RoleDto;
import org.lekitech.gafalag.entity.v2.security.Role;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting {@link Role} entities to {@link RoleDto}.
 * This mapper is responsible for handling the transformation of data between
 * the Role entity and its corresponding Data Transfer Object (DTO).
 * <p>
 * It utilizes MapStruct for automating the conversion process, ensuring
 * a clean and maintainable codebase for object mappings.</p>
 *
 * @see Role
 * @see RoleDto
 */
@Mapper(componentModel = "spring", uses = PermissionMapper.class)
interface RoleMapper {

    /**
     * Converts a {@link Role} entity to a {@link RoleDto} Data Transfer Object (DTO).
     *
     * @param role The {@link Role} entity to be converted.
     * @return A {@link RoleDto} representing the converted entity.
     */
    RoleDto toDto(Role role);
}
