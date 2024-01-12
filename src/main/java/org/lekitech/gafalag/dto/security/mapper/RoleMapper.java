package org.lekitech.gafalag.dto.security.mapper;

import org.lekitech.gafalag.dto.security.RoleDto;
import org.lekitech.gafalag.entity.v2.security.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting {@link Role} entities to {@link RoleDto}.
 * This mapper is responsible for handling the transformation of data between
 * the Role entity and its corresponding Data Transfer Object (DTO).
 *
 * <p>It utilizes MapStruct for automating the conversion process, ensuring
 * a clean and maintainable codebase for object mappings.</p>
 *
 * @see Role
 * @see RoleDto
 */
@Mapper(componentModel = "spring")
interface RoleMapper {

    /**
     * Converts a {@link Role} entity to a {@link RoleDto}.
     * This method maps the 'language.id' property of the Role entity
     * to the 'language' field in the RoleDto.
     *
     * <p>The conversion includes any necessary transformations required
     * to adapt the Role entity data structure to the RoleDto structure.</p>
     *
     * @param role The role entity to be converted.
     * @return A {@link RoleDto} with data mapped from the {@link Role} entity.
     */
    @Mapping(source = "role.language.id", target = "language")
    RoleDto toDto(Role role);

}
