package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.lekitech.gafalag.entity.v2.DefinitionDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
@Table(name = "language")
public class Language {

    @Id
    @Column(length = 3)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "iso_2", length = 2)
    private String iso2;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    //  - relations

    @OneToMany(mappedBy = "language", cascade = CascadeType.PERSIST)
    private Set<Dialect> dialects = new HashSet<>();

    @OneToMany(mappedBy = "language", cascade = CascadeType.PERSIST)
    private Set<DefinitionDetails> definitionDetails = new HashSet<>();

}
