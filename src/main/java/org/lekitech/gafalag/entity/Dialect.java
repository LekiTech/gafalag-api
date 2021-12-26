package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "dialect")
@EqualsAndHashCode(of = {"id", "name", "language"})
public class Dialect {

    @Id
    @SequenceGenerator(name = "dialect_seq", sequenceName = "dialect_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dialect_seq")
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
