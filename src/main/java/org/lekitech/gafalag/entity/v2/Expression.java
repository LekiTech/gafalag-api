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
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "expression")
@EqualsAndHashCode(of = {"id"})
public class Expression {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "spelling")
    private String spelling;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // - relations

    @OneToMany(mappedBy = "expression", cascade = CascadeType.PERSIST)
    private Set<MediaFile> mediaFiles = new HashSet<>();

    @OneToMany(mappedBy = "expression", cascade = CascadeType.PERSIST)
    private Set<ExpressionDetails> expressionDetails = new HashSet<>();

    public Expression(String spelling,
                      Language language,
                      Set<ExpressionDetails> expressionDetails) {
        this.spelling = spelling;
        this.language = language;
        this.expressionDetails = expressionDetails;
    }
}