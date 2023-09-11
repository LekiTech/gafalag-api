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
import java.util.HashSet;
import java.util.Set;
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
    private Set<DefinitionTag> definitionTags = new HashSet<>();

    public Definition(String value) {
        this.value = value;
    }
}
