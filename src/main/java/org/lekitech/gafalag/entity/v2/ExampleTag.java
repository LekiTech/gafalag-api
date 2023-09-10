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
@Table(name = "example_tag")
@EqualsAndHashCode(of = {"exampleTagId"})
public class ExampleTag {

    @EmbeddedId
    private ExampleTagId exampleTagId;

    @MapsId("tagAbbr")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_abbr")
    private Tag tag;

    @MapsId("exampleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "example_id")
    private Example example;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public ExampleTag(Tag tag, Example example) {
        this.exampleTagId = new ExampleTagId(tag.getAbbreviation(), example.getId());
        this.tag = tag;
        this.example = example;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"tagAbbr", "exampleId"})
    public static class ExampleTagId implements Serializable {

        @Column(name = "tag_abbr")
        private String tagAbbr;

        @Column(name = "example_id")
        private UUID exampleId;

    }

}
