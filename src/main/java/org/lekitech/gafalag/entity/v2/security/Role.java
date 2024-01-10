package org.lekitech.gafalag.entity.v2.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
@EqualsAndHashCode(of = {"id"})
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    @Type(type = "org.lekitech.gafalag.entity.v2.typemapping.EnumTypePostgreSql")
    private RoleType authority;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Role(RoleType authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority.name();
    }

    public enum RoleType {
        ADMIN,
        LINGUIST,
        USER
    }
}
