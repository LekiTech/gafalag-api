package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "source")
public class Source {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    @Column(unique = true)
    private String name;

    @Column()
    private String url;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public Source(String name, Optional<String> url) {
        this.name = name;
        url.ifPresent(value -> this.url = value);
    }
}
