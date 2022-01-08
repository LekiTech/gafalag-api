package org.lekitech.gafalag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "definition")
public class Definition implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    @Column(name = "definition_text")
    private String text;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_id")
    private Expression expression;

    @ManyToOne(fetch = FetchType.LAZY)
    private PartOfSpeech partOfSpeech;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Source source;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public Definition(String text, Language language, Source source) {
        this.text = text;
        this.language = language;
        this.source = source;
    }

    public Definition(String text, Language language, Source source, Expression expression) {
        this.text = text;
        this.language = language;
        this.source = source;
        this.expression = expression;
    }
}
