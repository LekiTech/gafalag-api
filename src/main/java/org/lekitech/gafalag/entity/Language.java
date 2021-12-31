package org.lekitech.gafalag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "language")
public class Language {

    @Id
    @SequenceGenerator(name = "language_seq", sequenceName = "language_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_seq")
    public Long id;

    @NonNull
    @Column(unique = true)
    public String name;

    @NonNull
    @Column(unique = true, name = "iso639_2")
    public String iso2;

    @NonNull
    @Column(unique = true, name = "iso639_3")
    public String iso3;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;

    @OneToMany(mappedBy = "language", orphanRemoval = true, fetch = FetchType.LAZY)
    public Set<Dialect> dialects;

    @JsonIgnore
    @OneToMany(mappedBy = "language", orphanRemoval = true, fetch = FetchType.LAZY)
    public Set<Expression> expressions;
}
