package org.lekitech.gafalag.entity.v2.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
 * Entity representing a user in the security context.
 * This class implements {@link UserDetails} from Spring Security to integrate with
 * the authentication and authorization mechanisms.
 *
 * <p>It includes information like the user's name, email, password, and roles (authorities),
 * as well as timestamps for creation and updates.</p>
 *
 * @see UserDetails
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user", schema = "public")
@EqualsAndHashCode(of = {"id", "email"})
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "verified")
    private Boolean verified;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // Relations

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> authorities;

    /**
     * Constructs a new User with specified details.
     *
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param email       Email of the user, used as the username in the authentication process.
     * @param password    Password of the user for authentication.
     * @param authorities Set of roles or authorities assigned to the user.
     */
    public User(String firstName,
                String lastName,
                String email,
                String password,
                Set<Role> authorities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.verified = false;
    }

    /**
     * @return Email as the username for Spring Security.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /* Other overridden methods from UserDetails interface */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
