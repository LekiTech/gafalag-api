package org.lekitech.gafalag.entity.v2.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.lekitech.gafalag.entity.v2.Language;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Entity representing a role in the security context of the application.
 * This class implements {@link GrantedAuthority} from Spring Security, enabling it
 * to represent an authority granted to a {@link UserDetails}.
 *
 * <p>A role is characterized by its unique authority, associated language, and permissions.</p>
 *
 * @see GrantedAuthority
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
@EqualsAndHashCode(of = {"id", "authority", "language"})
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "authority", unique = true)
    private String authority;

    /**
     * Enum representing the permission level of the role.
     * Available permissions include EDIT and VIEW.
     */
    @Enumerated(EnumType.STRING)
    @Type(type = "org.lekitech.gafalag.entity.v2.typemapping.EnumTypePostgreSql")
    @Column(name = "permission")
    private Permission permission;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    /**
     * Represents the language associated with the role.
     */
    @OneToOne(fetch = FetchType.LAZY)
    private Language language;

    /**
     * Constructs a new Role with specified details.
     *
     * @param authority  Unique name representing the role.
     * @param language   Language associated with the role.
     * @param permission Permission level of the role.
     */
    public Role(String authority, Language language, Permission permission) {
        this.authority = authority;
        this.language = language;
        this.permission = permission;
    }

    /**
     * Enum type for defining role permissions.
     * EDIT allows for editing capabilities, while VIEW is more restrictive.
     */
    public enum Permission {
        EDIT,
        VIEW
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}
