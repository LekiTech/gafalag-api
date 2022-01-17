package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "spelling"})
@Table(name = "expression")
public class Expression {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "spelling")
    private String spelling;

    @Column(name = "misspelling")
    private Boolean misspelling;

    @Column(name = "inflection")
    private String inflection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialect_id")
    private Dialect dialect;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "expression", cascade = CascadeType.PERSIST)
    private List<Definition> definitions = new ArrayList<>();

    public Expression(@NonNull String spelling,
                      Optional<String> inflection,
                      @NonNull Language language,
                      List<Definition> definitions) {
        this.spelling = spelling;
        inflection.ifPresent(value -> this.inflection = value);
        this.language = language;
        definitions.forEach(def -> def.setExpression(this));
        this.definitions = definitions;
    }
}
