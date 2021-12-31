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
@EqualsAndHashCode(of = {
        "id",
        "name",
        "iso639_2",
        "iso639_3"
})
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "iso639_2")
    private String iso639_2;

    @NonNull
    @Column(name = "iso639_3")
    private String iso639_3;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private Set<Dialect> dialects;

    @OneToMany(mappedBy = "language")
    private Set<Expression> expressions;
}
