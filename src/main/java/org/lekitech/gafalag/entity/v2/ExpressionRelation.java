package org.lekitech.gafalag.entity.v2;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "expression_relation")
@EqualsAndHashCode(of = {"expressionRelationId"})
public class ExpressionRelation {

    @EmbeddedId
    private ExpressionRelationId expressionRelationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_1_id")
    private Expression expression1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_2_id")
    private Expression expression2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relation_type_id")
    private RelationType relationType;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public ExpressionRelation(Expression expression1, Expression expression2, RelationType relationType) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.relationType = relationType;
        this.expressionRelationId = new ExpressionRelationId(
                expression1.getId(), expression2.getId(), relationType.getId()
        );
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"expression1Id", "expression2Id", "relationTypeId"})
    public static class ExpressionRelationId implements Serializable {

        @Column(name = "expression_1_id")
        private UUID expression1Id;

        @Column(name = "expression_2_id")
        private UUID expression2Id;

        @Column(name = "relation_type_id")
        private UUID relationTypeId;

    }

}
