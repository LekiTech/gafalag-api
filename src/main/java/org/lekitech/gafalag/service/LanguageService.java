package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.Language;
import org.lekitech.gafalag.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LanguageService {

    private final LanguageRepository repository;

    public Language getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Language save(Language language) {
        return repository.save(language);
    }
}
