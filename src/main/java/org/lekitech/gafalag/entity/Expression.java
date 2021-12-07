package org.lekitech.gafalag.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expression")
public class Expression {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String spelling;

    private Boolean misspelling;

    private String inflection;

    @ManyToOne
    @JoinColumn(name = "gender_id", updatable = false)
    private Gender gender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "language_id", updatable = false, nullable = false)
    private Language language;

    @ManyToOne
    @JoinColumn(name = "dialect_id", updatable = false)
    private Dialect dialect;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public Expression(
            String spelling,
            Boolean misspelling,
            String inflection,
            Gender gender,
            Language language,
            Dialect dialect
    ) {
        this.spelling = spelling;
        this.misspelling = misspelling;
        this.inflection = inflection;
        this.gender = gender;
        this.language = language;
        this.dialect = dialect;
    }
}
