package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.Dictionary;
import org.lekitech.gafalag.dto.v2.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryService {

    public Expression save(Expression request) {
        return null; // TODO: 9/9/23 implement
    }

    public void saveBatch(Dictionary request) {
        // TODO: 9/9/23 implement
    }
}
