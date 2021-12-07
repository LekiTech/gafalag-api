package org.lekitech.gafalag.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dialect")
public class Dialect {

    @Id
    @SequenceGenerator(name = "dialect_seq", sequenceName = "dialect_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dialect_seq")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "language_id", updatable = false, nullable = false)
    private Language language;

    @NotNull
    @Column(unique = true)
    private String name;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
