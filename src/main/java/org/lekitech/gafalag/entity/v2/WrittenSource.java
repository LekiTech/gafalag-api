package org.lekitech.gafalag.entity.v2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "written_source")
@EqualsAndHashCode(of = {"id"})
public class WrittenSource {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    // TODO: rename `name` to `title` and fix this in `SourceMapperV2` afterwards
    @Column(name = "name")
    private String name;

    @Column(name = "authors")
    private String authors;

    @Column(name = "publication_year")
    private String publicationYear;

    @Column(name = "provided_by")
    private String providedBy;

    @Column(name = "provided_by_url")
    private String providedByUrl;

    @Column(name = "processed_by")
    private String processedBy;

    @Column(name = "copyright")
    private String copyright;

    @Column(name = "see_source_url")
    private String seeSourceUrl;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public WrittenSource(Source source,
                         String name,
                         String authors,
                         String publicationYear,
                         String providedBy,
                         String providedByUrl,
                         String processedBy,
                         String copyright,
                         String seeSourceUrl,
                         String description) {
        this.source = source;
        this.name = name;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.providedBy = providedBy;
        this.providedByUrl = providedByUrl;
        this.processedBy = processedBy;
        this.copyright = copyright;
        this.seeSourceUrl = seeSourceUrl;
        this.description = description;
    }
}
