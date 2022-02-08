package org.lekitech.gafalag.entity;

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
@Table(name = "definition_category")
public class DefinitionCategory {

    @EmbeddedId
    private DefinitionCategoryId definitionCategoryId;

    @ManyToOne
    @JoinColumn(name = "definition_id", insertable = false, updatable = false)
    private Definition definition;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public DefinitionCategory(Definition definition, Category category) {
        this.definition = definition;
        this.category = category;
        this.definitionCategoryId = new DefinitionCategoryId(
                definition.getId(), category.getId()
        );
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"definitionId", "categoryId"})
    public static class DefinitionCategoryId implements Serializable {

        @Column(name = "definition_id")
        UUID definitionId;

        @Column(name = "category_id")
        UUID categoryId;
    }
}
