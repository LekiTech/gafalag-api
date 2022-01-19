package org.lekitech.gafalag.entity;

import lombok.*;
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
@EqualsAndHashCode(of = {"id", "name"})
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(generator = "language_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "iso639_2", length = 2)
    private String iso2;

    @Column(name = "iso639_3", length = 3)
    private String iso3;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "language", cascade = CascadeType.PERSIST)
    private Set<Dialect> dialects = new HashSet<>();

    public Language(String name,
                    String iso2,
                    String iso3) {
        this.name = name;
        this.iso2 = iso2;
        this.iso3 = iso3;
    }
}
