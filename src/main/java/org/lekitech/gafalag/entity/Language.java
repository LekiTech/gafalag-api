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
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "iso2", "iso3"})
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(generator = "language_id_seq")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "iso639_2", length = 2)
    private String iso2;

    @NonNull
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

    @OneToMany(mappedBy = "language", cascade = CascadeType.PERSIST)
    private Set<Expression> expressions = new HashSet<>();
}
