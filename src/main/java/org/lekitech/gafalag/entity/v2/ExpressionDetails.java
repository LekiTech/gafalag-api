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

    @OneToMany(mappedBy = "expressionDetails", cascade = CascadeType.PERSIST)
    private List<ExpressionMatchDetails> expressionMatchDetails = new ArrayList<>();

    @OneToMany(mappedBy = "expressionDetails", cascade = CascadeType.PERSIST)
    private List<ExpressionExample> expressionExamples = new ArrayList<>();

    public ExpressionDetails(String gr,
                             String inflection,
                             Source source) {
        this.gr = gr;
        this.inflection = inflection;
        this.source = source;
    }

    public void addExpressionExamples(List<ExpressionExample> expressionExampleEntities) {
        for (ExpressionExample expressionExampleEntity : expressionExampleEntities) {
            expressionExampleEntity.setExpressionDetails(this);
        }
        this.expressionExamples = expressionExampleEntities;
    }

    public void addExpressionMatchDetails(List<ExpressionMatchDetails> expressionMatchDetailsEntities) {
        this.expressionMatchDetails = expressionMatchDetailsEntities;
    }
}
