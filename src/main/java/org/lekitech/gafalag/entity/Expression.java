package org.lekitech.gafalag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "expression")
public class Expression implements Serializable {

    @Id
    @GeneratedValue
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Gender gender;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Dialect dialect;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    // Related tables
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "expression", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Definition> definitions = new java.util.ArrayList<>();

    public Expression(String spelling,
                      Optional<String> inflection,
                      Language language,
                      List<Definition> definitions) {
        this.spelling = spelling;
        inflection.ifPresent(value -> this.inflection = value);
        this.language = language;
        definitions.forEach(def -> def.setExpression(this));
        this.definitions = definitions;
    }
}
