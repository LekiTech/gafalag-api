package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.Source;
import org.lekitech.gafalag.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SourceService {
    private final SourceRepository sourceRepository;

    public Source save(Source source) {
        return sourceRepository.save(source);
    }

    public Source getById(UUID id) {
        return sourceRepository.findById(id).orElseThrow();
    }

    public Source getByName(String name) {
        return sourceRepository.findByName(name).orElseThrow();
    }

    public Source getOrCreate(String name, Optional<String> url) {
        try {
            return sourceRepository.findByName(name).orElseThrow();
        } catch(Exception ex) {
            var source = new Source(name, url);
            return sourceRepository.save(source);
        }
    }
}
