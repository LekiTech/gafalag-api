package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Language;
import org.lekitech.gafalag.entity.v2.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("transactionManagerV2")
public interface TagRepository extends JpaRepository<Tag, String> {

}
