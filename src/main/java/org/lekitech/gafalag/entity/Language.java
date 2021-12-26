package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "iso639_2", "iso639_3"})
@Table(name = "language", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "iso639_2", "iso639_3"}))
public class Language {

    @Id
    @SequenceGenerator(name = "language_seq", sequenceName = "language_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_seq")
    private Long id;

    @NonNull
    @Column(unique = true)
    private String name;

    @NonNull
    @Column(unique = true)
    private String iso639_2;

    @NonNull
    @Column(unique = true)
    private String iso639_3;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "language", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Dialect> dialects;

    @OneToMany(mappedBy = "language", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Expression> expressions;
}
