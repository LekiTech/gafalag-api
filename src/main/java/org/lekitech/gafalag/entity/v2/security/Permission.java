package org.lekitech.gafalag.entity.v2.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.lekitech.gafalag.entity.v2.Language;

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

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "permission")
@EqualsAndHashCode(of = {"id", "right", "component", "language"})
public class Permission {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Type(type = "org.lekitech.gafalag.entity.v2.typemapping.EnumTypePostgreSql")
    @Column(name = "right")
    private Right right;

    @Enumerated(EnumType.STRING)
    @Type(type = "org.lekitech.gafalag.entity.v2.typemapping.EnumTypePostgreSql")
    @Column(name = "component")
    private Component component;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    // Relations

    @OneToOne(fetch = FetchType.LAZY)
    Language language;

    public Permission(Right right,
                      Component component,
                      Language language) {
        this.right = right;
        this.component = component;
        this.language = language;
    }

    public enum Right {
        ADD,
        DELETE,
        EDIT,
        READ
    }

    public enum Component {
        USER,
        WRITTEN_SOURCE,
        EXPRESSION,
        LANGUAGE
    }
}
