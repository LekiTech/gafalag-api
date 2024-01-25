package org.lekitech.gafalag.entity.v2.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;
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
@EqualsAndHashCode(of = {"id", "name", "permissions"})
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Override
    public String getAuthority() {
        return name;
    }

}
