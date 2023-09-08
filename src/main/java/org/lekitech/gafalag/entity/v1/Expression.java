package org.lekitech.gafalag.entity.v1;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "spelling", "language"})
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

    public Expression(String spelling,
                      String inflection,
                      Language language,
                      List<Definition> definitions) {
        this.spelling = spelling;
        this.inflection = inflection;
        this.language = language;
        definitions.forEach(this::reference);
    }

    private void reference(Definition definition) {
        definition.setExpression(this);
        definitions.add(definition);
    }
}
