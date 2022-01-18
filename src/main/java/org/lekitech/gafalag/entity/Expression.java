package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id", "spelling"})
@Table(name = "expression")
public class Expression {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    @Column(name = "spelling")
    private String spelling;

    @Column(name = "misspelling")
    private Boolean misspelling;

    @Column(name = "inflection")
    private String inflection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialect_id")
    private Dialect dialect;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "expression", cascade = CascadeType.PERSIST)
    private List<Definition> definitions = new ArrayList<>();
}
