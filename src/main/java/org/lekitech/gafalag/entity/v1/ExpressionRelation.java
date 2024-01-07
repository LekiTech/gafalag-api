package org.lekitech.gafalag.entity.v1;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@Immutable
@NoArgsConstructor
@Table(name = "expression_relation")
public class ExpressionRelation implements Serializable {

    @EmbeddedId
    private ExpressionRelationId expressionRelationId;

    @ManyToOne
    @JoinColumn(name = "expression_1_id", insertable = false, updatable = false)
    private Expression expression1;

    @ManyToOne
    @JoinColumn(name = "expression_2_id", insertable = false, updatable = false)
    private Expression expression2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relation_type_id")
    private RelationType relationType;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"expression1Id", "expression2Id"})
    public static class ExpressionRelationId implements Serializable {

        @Column(name = "expression_1_id")
        UUID expression1Id;

        @Column(name = "expression_2_id")
        UUID expression2Id;
    }
}
