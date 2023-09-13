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
@Table(name = "expression_match_details")
@EqualsAndHashCode(of = {"expressionMatchDetailsId"})
public class ExpressionMatchDetails {

    @EmbeddedId
    private ExpressionMatchDetailsId expressionMatchDetailsId;

    @MapsId("expressionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_id")
    private Expression expression;

    @MapsId("expressionDetailsId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_details_id")
    private ExpressionDetails expressionDetails;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public ExpressionMatchDetails(Expression expression, ExpressionDetails expressionDetails) {
        this.expression = expression;
        this.expressionDetails = expressionDetails;
        this.expressionMatchDetailsId = new ExpressionMatchDetailsId(
                expression.getId(), expressionDetails.getId()
        );
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"expressionId", "expressionDetailsId"})
    public static class ExpressionMatchDetailsId implements Serializable {

        @Column(name = "expression_id")
        private UUID expressionId;

        @Column(name = "expression_details_id")
        private UUID expressionDetailsId;

    }

}
