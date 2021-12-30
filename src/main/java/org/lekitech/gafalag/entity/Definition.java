package org.lekitech.gafalag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "definition")
public class Definition {
    @Id
    @GeneratedValue(generator = UUIDGenerator.UUID_GEN_STRATEGY)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    public UUID id;

    @NonNull
    @Column(name="definition_text")
    public String text;

    @NonNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="expression_id")
    public Expression expression;

    @ManyToOne(fetch = FetchType.EAGER)
    public PartOfSpeech partOfSpeech;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    public Language language;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    public Source source;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;

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
