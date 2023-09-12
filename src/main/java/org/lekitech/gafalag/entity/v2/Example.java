package org.lekitech.gafalag.entity.v2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "example")
@EqualsAndHashCode(of = {"id"})
public class Example {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "source")
    private String source;

    @Column(name = "translation")
    private String translation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_lang_id")
    private Language srcLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trl_lang_id")
    private Language trlLanguage;

    @Column(name = "raw")
    private String raw;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    //  - relations

    @OneToMany(mappedBy = "example", cascade = CascadeType.PERSIST)
    private List<DefinitionExample> definitionExamples = new ArrayList<>();

    @OneToMany(mappedBy = "example", cascade = CascadeType.PERSIST)
    private List<ExpressionExample> expressionExamples = new ArrayList<>();

    @OneToMany(mappedBy = "example", cascade = CascadeType.PERSIST)
    private List<ExampleTag> exampleTags = new ArrayList<>();

    public Example(String source,
                   String translation,
                   Language srcLanguage,
                   Language trlLanguage,
                   String raw) {
        this.source = source;
        this.translation = translation;
        this.srcLanguage = srcLanguage;
        this.trlLanguage = trlLanguage;
        this.raw = raw;
    }

    public void addExampleTags(List<ExampleTag> exampleTagEntities) {
        exampleTagEntities.forEach(exampleTag -> exampleTag.setExample(this));
        this.exampleTags = exampleTagEntities;
    }
}
