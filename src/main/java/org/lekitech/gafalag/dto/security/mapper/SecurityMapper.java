package org.lekitech.gafalag.dto.security.mapper;

import org.lekitech.gafalag.dto.security.RoleDto;
import org.lekitech.gafalag.dto.security.UserDto;
import org.lekitech.gafalag.entity.v2.security.Role;
import org.lekitech.gafalag.entity.v2.security.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper interface for converting security-related entities to their corresponding DTOs.
 * This mapper facilitates the conversion of {@link User} and {@link Role} entities into {@link UserDto}
 * and {@link RoleDto} respectively.
 *
 * <p>The mapper is designed to abstract the complexity involved in manually converting entities
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
     * Converts a {@link User} entity into a {@link UserDto}.
     *
     * <p>This conversion process excludes the 'password' field from the {@link User} entity
     * to prevent exposure of sensitive information in the DTO. The method maps other relevant
     * fields from the User entity to their corresponding fields in the UserDto.</p>
     *
     * @param user The user entity to be converted.
     * @return A {@link UserDto} populated with data from the {@link User} entity.
     */
    @Mapping(ignore = true, target = "password")
    UserDto toDto(User user);

}
