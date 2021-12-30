package org.lekitech.gafalag.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "source")
public class Source {

    @Id
    @GeneratedValue(generator = UUIDGenerator.UUID_GEN_STRATEGY)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    public UUID id;

    @NonNull
    @Column(unique = true)
    public String name;

    @Column()
    public String url;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;

    public Source(String name, Optional<String> url) {
        this.name = name;
        url.ifPresent(value -> this.url = value);
    }
}
