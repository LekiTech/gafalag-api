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
@Table(name = "language")
@EqualsAndHashCode(of = {"id"})
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
    private Set<GrammaticalCase> grammaticalCases = new HashSet<>();

    @OneToMany(mappedBy = "language", cascade = CascadeType.PERSIST)
    private Set<Dialect> dialects = new HashSet<>();

}
