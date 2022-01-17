package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.lekitech.gafalag.entity.type.Mediatype;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "mediafile")
public class Mediafile {

    @Id
    @GeneratedValue(generator = "mediafile_id_seq")
    private Long id;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Mediatype mediatype;

    @NonNull
    private String url;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
