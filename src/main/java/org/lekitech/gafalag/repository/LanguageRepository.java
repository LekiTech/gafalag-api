package org.lekitech.gafalag.repository;

import org.lekitech.gafalag.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {

    Optional<Language> findByIso2(String iso2);
}
