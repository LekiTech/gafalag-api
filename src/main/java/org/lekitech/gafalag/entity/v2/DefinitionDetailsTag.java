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
@Table(name = "definition_details_tag")
@EqualsAndHashCode(of = {"definitionDetailsTagId"})
public class DefinitionDetailsTag {

    @EmbeddedId
    private DefinitionDetailsTagId definitionDetailsTagId;

    @MapsId("tagAbbr")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_abbr", referencedColumnName = "abbreviation")
    private Tag tag;

    @MapsId("definitionDetailsId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_details_id", referencedColumnName = "id")
    private DefinitionDetails definitionDetails;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public DefinitionDetailsTag(Tag tag, DefinitionDetails definitionDetails) {
        this.tag = tag;
        this.definitionDetails = definitionDetails;
        this.definitionDetailsTagId = new DefinitionDetailsTagId(
                tag.getAbbreviation(), definitionDetails.getId()
        );
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"tagAbbr", "definitionDetailsId"})
    public static class DefinitionDetailsTagId implements Serializable {

        @Getter
        @Column(name = "tag_abbr")
        private String tagAbbr;

        @Column(name = "definition_details_id")
        private UUID definitionDetailsId;

    }

}
