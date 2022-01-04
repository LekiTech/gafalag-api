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
@RequiredArgsConstructor(onConstructor = @__(@NonNull))
@Table(name = "language")
public class Language {

    @Id
    @NonNull
    @GeneratedValue
    public Long id;

    @NonNull
    @Column(name = "name")
    public String name;

    @NonNull
    @Column(name = "iso639_2")
    public String iso2;

    @NonNull
    @Column(name = "iso639_3")
    public String iso3;

    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp updatedAt;

    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    public Set<Dialect> dialects;

    @JsonIgnore
    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    public Set<Expression> expressions;
}
