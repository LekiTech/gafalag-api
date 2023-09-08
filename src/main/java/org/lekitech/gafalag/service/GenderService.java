package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.v1.Gender;
import org.lekitech.gafalag.repository.v1.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenderService {

    private final GenderRepository repository;

    public Gender save(Gender dialect) {
        return repository.save(dialect);
    }

    public Gender findById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
