package org.lekitech.gafalag.repository.v1;

import org.lekitech.gafalag.entity.v1.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional("transactionManagerV1")
public interface LanguageRepository extends JpaRepository<Language, String> {

    @PersistenceContext(unitName = "v1")
    Optional<Language> findByIso2(String iso2);
}
