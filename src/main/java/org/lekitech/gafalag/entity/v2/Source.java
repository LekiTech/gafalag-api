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
@Table(name = "source")
@EqualsAndHashCode(of = {"id"})
public class Source {

    public final static String USER = "USER";
    public final static String WRITTEN = "WRITTEN";

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "type")
    private String type;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    //  - relations

    @OneToMany(mappedBy = "source", cascade = CascadeType.PERSIST)
    private Set<ExpressionDetails> expressionDetails = new HashSet<>();

    @OneToMany(mappedBy = "source", cascade = CascadeType.PERSIST)
    private Set<WrittenSource> writtenSources = new HashSet<>();

//    @OneToMany(mappedBy = "source", cascade = CascadeType.PERSIST)
//    private Set<UserSource> users = new HashSet<>();

    public Source(String type) {
        this.type = type;
    }
}
