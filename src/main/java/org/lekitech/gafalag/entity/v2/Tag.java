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

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"abbreviation"})
@Table(name = "tag")
public class Tag {

    public static final String CATEGORY = "CATEGORY";
    public static final String GRAMMAR = "GRAMMAR";
    public static final String ETYMOLOGY = "ETYMOLOGY";
    public static final String UNKNOWN = "UNKNOWN";

    @Id
    @Column(name = "abbreviation", length = 10)
    private String abbreviation;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // - relations

    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST)
    private Set<DefinitionDetailsTag> definitionDetailsTags = new HashSet<>();

    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST)
    private Set<DefinitionTag> definitionTags = new HashSet<>();

    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST)
    private Set<ExampleTag> exampleTags = new HashSet<>();

    public Tag(String abbreviation) {
        this.abbreviation = abbreviation;
        this.type = UNKNOWN;
    }
}