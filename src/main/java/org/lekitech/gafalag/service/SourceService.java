package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.v1.Source;
import org.lekitech.gafalag.repository.v1.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceService {

    private final SourceRepository sourceRepository;

    public Source save(Source source) {
        return sourceRepository.save(source);
    }

    public List<Source> getAll() {
        return sourceRepository.findAll();
    }

    public Source getById(UUID id) {
        return sourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Source is not found, id=" + id));
    }

    public Source getByName(String name) {
        return sourceRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Source is not found, name=" + name));
    }

    public Source getOrCreate(String name, String url) {
        return sourceRepository
                .findByName(name)
                .orElse(sourceRepository.save(new Source(name, url)));
    }
}
