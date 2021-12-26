package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.id.UUIDGenerator;
import org.lekitech.gafalag.enumeration.Gender;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "expression")
public class Expression {

    @Id
    @GeneratedValue(generator = UUIDGenerator.UUID_GEN_STRATEGY)
    @Type(type = "pg-uuid")
    private UUID id;

    @NonNull
    @Column
    private String spelling;

    @NonNull
    @Column
    private Boolean misspelling;

    @NonNull
    @Column
    private String inflection;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Language language;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Dialect dialect;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
