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
@Table(name = "definition_tag")
@EqualsAndHashCode(of = {"definitionTagId"})
public class DefinitionTag {

    @EmbeddedId
    private DefinitionTagId definitionTagId;

    @MapsId("tagAbbr")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_abbr")
    private Tag tag;

    @MapsId("definitionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id")
    private Definition definition;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public DefinitionTag(Tag tag, Definition definition) {
        this.tag = tag;
        this.definition = definition;
        this.definitionTagId = new DefinitionTagId(
                tag.getAbbreviation(), definition.getId()
        );
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"tagAbbr", "definitionId"})
    public static class DefinitionTagId implements Serializable {

        @Column(name = "tag_abbr")
        private String tagAbbr;

        @Column(name = "definition_id")
        private UUID definitionId;

    }

}
