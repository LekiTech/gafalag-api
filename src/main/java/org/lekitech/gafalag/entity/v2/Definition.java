package org.lekitech.gafalag.entity.v2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Qualifier("v2")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "definition")
@EqualsAndHashCode(of = {"id"})
public class Definition {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_details_id")
    private DefinitionDetails definitionDetails;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // - relations

    @OneToMany(mappedBy = "definition", cascade = CascadeType.PERSIST)
    private List<DefinitionTag> definitionTags = new ArrayList<>();

    public Definition(String value,
                      DefinitionDetails definitionDetails) {
        this.value = value;
        this.definitionDetails = definitionDetails;
    }

    public void addDefinitionTags(List<DefinitionTag> definitionTagList) {
        definitionTagList.forEach(definitionTag -> definitionTag.setDefinition(this));
        setDefinitionTags(definitionTagList);
    }
}
