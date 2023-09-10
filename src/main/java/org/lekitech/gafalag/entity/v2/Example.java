package org.lekitech.gafalag.entity.v2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
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
    private Set<DefinitionExample> definitionExamples = new HashSet<>();

    @OneToMany(mappedBy = "example", cascade = CascadeType.PERSIST)
    private Set<ExpressionExample> expressionExamples = new HashSet<>();

    @OneToMany(mappedBy = "example", cascade = CascadeType.PERSIST)
    private Set<ExampleTag> exampleTags = new HashSet<>();

}
