package org.lekitech.gafalag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.id.UUIDGenerator;
import org.lekitech.gafalag.enumeration.Gender;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "expression")
public class Expression {

    @Id
    @GeneratedValue(generator = UUIDGenerator.UUID_GEN_STRATEGY)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    public UUID id;

    @NonNull
    @Column
    public String spelling;

    @NonNull
    @Column
    public Boolean misspelling;

    @NonNull
    @Column
    public String inflection;

    @NonNull
    @Enumerated(EnumType.STRING)
    public Gender gender;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    public Language language;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    public Dialect dialect;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;

    // Related tables
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "expression")
    public List<Definition> definitions;

    public Expression(String spelling, Optional<String> inflection, Language language, List<Definition> definitions) {
        this.spelling = spelling;
        inflection.ifPresent(value -> this.inflection = value);
        this.language = language;
        this.definitions = definitions;
        // Important for cascade persistence
        for (var definition : definitions) {
            definition.expression = this;
        }
    }
}
