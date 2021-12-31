package org.lekitech.gafalag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "expression")
public class Expression implements Serializable {

    @Id
    @GeneratedValue
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
    @ManyToOne(fetch = FetchType.LAZY)
    public Gender gender;

    @JsonIgnore
    @Column(name = "gender_id", insertable = false, updatable = false)
    public Long genderId;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    public Language language;
    @JsonIgnore
    @Column(name = "language_id", insertable = false, updatable = false)
    public Long languageId;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Dialect dialect;
    @JsonIgnore
    @Column(name = "dialect_id", insertable = false, updatable = false)
    public Long dialectId;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;

    // Related tables
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "expression", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Definition> definitions = new java.util.ArrayList<>();

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
