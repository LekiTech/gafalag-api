package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.Language;
import org.lekitech.gafalag.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LanguageService {

    private final LanguageRepository repository;

    public Language save(Language language) {
        return repository.save(language);
    }

    public Language getById(String id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Language> getAll() {
        return repository.findAll();
    }

    public Language getByIso2(String iso2) {
        return repository.findByIso2(iso2)
                .orElseThrow(() -> new EntityNotFoundException("iso639_alpha_2=" + iso2));
    }
}
