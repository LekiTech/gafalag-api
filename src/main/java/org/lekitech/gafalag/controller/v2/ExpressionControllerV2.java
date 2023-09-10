package org.lekitech.gafalag.controller.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.ExpressionDto;
import org.lekitech.gafalag.service.v2.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(path = "v2/expression")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpressionControllerV2 {

    private final DictionaryService dictionaryService;

    @PostMapping
    public void saveExpression(@RequestBody ExpressionDto request) {
        try {
            dictionaryService.save(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
