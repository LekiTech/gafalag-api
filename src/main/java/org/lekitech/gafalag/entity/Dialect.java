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
    public Long id;

    @NonNull
    public String name;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    public Language language;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;
}
