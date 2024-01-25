package org.lekitech.gafalag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.v1.Dialect;
import org.lekitech.gafalag.repository.v1.DialectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialectService {

    private final DialectRepository dialectRepository;

    public Dialect save(Dialect dialect) {
        return dialectRepository.save(dialect);
    }

    public Dialect getById(Long id) {
        return dialectRepository.findById(id).orElseThrow();
    }
}
