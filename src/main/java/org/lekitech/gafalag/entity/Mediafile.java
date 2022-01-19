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
@EqualsAndHashCode(of = {"id", "url"})
@Table(name = "mediafile")
public class Mediafile {

    @Id
    @GeneratedValue(generator = "mediafile_id_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mediatype")
    private Mediatype mediatype;

    @Column(name = "url")
    private String url;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Mediafile(Mediatype mediatype,
                     String url) {
        this.mediatype = mediatype;
        this.url = url;
    }
}
