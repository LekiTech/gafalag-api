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
@Table(name = "definition_example")
@EqualsAndHashCode(of = {"definitionExampleId"})
public class DefinitionExample {

    @EmbeddedId
    private DefinitionExampleId definitionExampleId;

    @MapsId("exampleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "example_id")
    private Example example;

    @MapsId("definitionDetailsId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_details_id")
    private DefinitionDetails definitionDetails;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public DefinitionExample(Example example, DefinitionDetails definitionDetails) {
        this.example = example;
        this.definitionDetails = definitionDetails;
        this.definitionExampleId = new DefinitionExampleId(
                example.getId(), definitionDetails.getId()
        );
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"exampleId", "definitionDetailsId"})
    public static class DefinitionExampleId implements Serializable {

        @Column(name = "example_id")
        private UUID exampleId;

        @Column(name = "definition_details_id")
        private UUID definitionDetailsId;

    }

}
