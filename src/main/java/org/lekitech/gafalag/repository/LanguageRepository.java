package org.lekitech.gafalag.repository;

import org.lekitech.gafalag.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByIso2(String iso639_2);
    Optional<Language> findByIso3(String iso639_3);
}
