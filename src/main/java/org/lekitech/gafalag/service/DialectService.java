package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.Dialect;
import org.lekitech.gafalag.repository.DialectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DialectService {

    private final DialectRepository dialectRepository;

    public Dialect save(Dialect dialect) {
        return dialectRepository.save(dialect);
    }

    public Dialect findById(Long id) {
        return dialectRepository.findById(id).orElseThrow();
    }
}
