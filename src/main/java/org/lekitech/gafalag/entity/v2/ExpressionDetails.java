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
@Table(name = "expression_details")
@EqualsAndHashCode(of = {"id"})
public class ExpressionDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "gr")
    private String gr;

    @Column(name = "inflection")
    private String inflection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    //  - relations

    @OneToOne(mappedBy = "expressionDetails", cascade = CascadeType.PERSIST)
    private ExpressionMatchDetails expressionMatchDetails;

    @OneToMany(mappedBy = "expressionDetails", cascade = CascadeType.PERSIST)
    private List<ExpressionExample> expressionExamples = new ArrayList<>();

    @OneToMany(mappedBy = "expressionDetails", cascade = CascadeType.PERSIST)
    private List<DefinitionDetails> definitionDetails = new ArrayList<>();

    public ExpressionDetails(String gr,
                             String inflection,
                             Source source) {
        this.gr = gr;
        this.inflection = inflection;
        this.source = source;
    }

    public void addExpressionExamples(List<ExpressionExample> expressionExampleEntities) {
        expressionExampleEntities.forEach(expressionExample -> expressionExample.setExpressionDetails(this));
        setExpressionExamples(expressionExampleEntities);
    }

    public void addDefinitionDetails(List<DefinitionDetails> definitionDetailsEntities) {
        definitionDetailsEntities.forEach(definitionDetailsEntity -> definitionDetailsEntity.setExpressionDetails(this));
        setDefinitionDetails(definitionDetailsEntities);
    }
}
