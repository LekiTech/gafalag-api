package org.lekitech.gafalag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(generator = "language_id_seq")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "iso639_2")
    private String iso2;

    @NonNull
    @Column(name = "iso639_3")
    private String iso3;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private Set<Dialect> dialects;

    @JsonIgnore
    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private Set<Expression> expressions;
}
