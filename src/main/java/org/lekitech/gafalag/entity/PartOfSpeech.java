package org.lekitech.gafalag.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "part_of_speech")
public class PartOfSpeech {

    @Id
    @GeneratedValue(generator = "part_of_speech_id_seq")
    public Long id;

    @NonNull
    @Column(unique = true)
    public String name;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;
}
