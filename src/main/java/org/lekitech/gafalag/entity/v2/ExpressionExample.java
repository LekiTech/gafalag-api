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
@Table(name = "expression_example")
@EqualsAndHashCode(of = {"expressionExampleId"})
public class ExpressionExample {

    @EmbeddedId
    private ExpressionExampleId expressionExampleId;

    @MapsId("expressionDetailsId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_details_id")
    private ExpressionDetails expressionDetails;

    @MapsId("exampleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "example_id")
    private Example example;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public ExpressionExample(ExpressionDetails expressionDetails, Example example) {
        this.expressionDetails = expressionDetails;
        this.example = example;
        this.expressionExampleId = new ExpressionExampleId(
                expressionDetails.getId(), example.getId()
        );
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"expressionDetailsId", "exampleId"})
    public static class ExpressionExampleId implements Serializable {

        @Column(name = "expression_details_id")
        private UUID expressionDetailsId;

        @Column(name = "example_id")
        private UUID exampleId;

    }

}
