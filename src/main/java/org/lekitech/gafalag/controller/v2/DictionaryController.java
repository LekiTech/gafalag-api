package org.lekitech.gafalag.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.Dictionary;
import org.lekitech.gafalag.dto.v2.Expression;
import org.lekitech.gafalag.service.v2.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(path = "v2/dictionary")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PostMapping(path = "/batch", consumes = "multipart/form-data")
    public void saveDictionary(@RequestPart("file") MultipartFile file) {
        try {
            val mapper = new ObjectMapper().registerModule(new Jdk8Module());
            dictionaryService.saveBatch(mapper.readValue(
                    file.getBytes(), Dictionary.class
            ));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public void saveExpression(@RequestBody Expression request) {
        dictionaryService.save(request);
    }

}
