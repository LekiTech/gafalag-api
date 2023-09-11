package org.lekitech.gafalag.entity.v2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "definition_details")
@EqualsAndHashCode(of = {"id"})
public class DefinitionDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_details_id")
    private ExpressionDetails expressionDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialect_id")
    private Dialect dialect;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    //  - relations

    @OneToMany(mappedBy = "definitionDetails", cascade = CascadeType.PERSIST)
    private Set<Definition> definitions = new HashSet<>();

    @OneToMany(mappedBy = "definitionDetails", cascade = CascadeType.PERSIST)
    private Set<DefinitionDetailsTag> definitionDetailsTags = new HashSet<>();

    @OneToMany(mappedBy = "definitionDetails", cascade = CascadeType.PERSIST)
    private Set<DefinitionExample> definitionExamples = new HashSet<>();

    public DefinitionDetails(ExpressionDetails expressionDetails,
                             Language language) {
        this.expressionDetails = expressionDetails;
        this.language = language;
    }
}
