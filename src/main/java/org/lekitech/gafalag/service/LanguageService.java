package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.Language;
import org.lekitech.gafalag.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LanguageService {

    private final LanguageRepository repository;

    public Language save(Language language) {
        return repository.save(language);
    }

    public Language getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Language> getAll() {
        return repository.findAll();
    }

    public Language getByIso2(String iso639_2) {
        if (iso639_2.length() != 2) {
            throw new StringIndexOutOfBoundsException("Language format 'ISO639_2' must be 2 chars long");
        }
        return repository.findByIso2(iso639_2).orElseThrow();
    }

    public Language getByIso3(String iso639_3) {
        if (iso639_3.length() != 3) {
            throw new StringIndexOutOfBoundsException("Language format 'ISO639_3' must be 3 chars long");
        }
        return repository.findByIso3(iso639_3).orElseThrow();
    }
}
