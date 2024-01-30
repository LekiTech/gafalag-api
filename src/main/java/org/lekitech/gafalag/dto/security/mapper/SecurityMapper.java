package org.lekitech.gafalag.dto.security.mapper;

import org.lekitech.gafalag.dto.security.RoleDto;
import org.lekitech.gafalag.dto.security.UserDto;
import org.lekitech.gafalag.entity.v2.security.Role;
import org.lekitech.gafalag.entity.v2.security.User;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper interface for converting security-related entities to their corresponding DTOs.
 * This mapper facilitates the conversion of {@link User} and {@link Role} entities into {@link UserDto}
 * and {@link RoleDto} respectively.
 * <p>
 * The mapper is designed to abstract the complexity involved in manually converting entities
 * to DTOs within the service layer, ensuring cleaner and more maintainable code.</p>
 *
 * @see User
 * @see Role
 * @see UserDto
 * @see RoleDto
 */
@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface SecurityMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDto} Data Transfer Object (DTO).
     *
     * @param user The {@link User} entity to be converted.
     * @return A {@link UserDto} representing the converted entity.
     */
    UserDto toDto(User user);
}
