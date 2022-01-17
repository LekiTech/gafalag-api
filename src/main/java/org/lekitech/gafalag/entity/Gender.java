package org.lekitech.gafalag.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "gender")
public class Gender {

    @Id
    @GeneratedValue(generator = "gender_id_seq")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "gender", cascade = CascadeType.PERSIST)
    private List<Expression> expressions = new ArrayList<>();
}
