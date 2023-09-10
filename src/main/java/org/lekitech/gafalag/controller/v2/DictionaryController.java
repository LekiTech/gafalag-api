package org.lekitech.gafalag.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.DictionaryDto;
import org.lekitech.gafalag.service.v2.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(path = "v2/dictionary")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PostMapping(consumes = "multipart/form-data")
    public void saveDictionary(@RequestPart("file") MultipartFile file) {
        try {
            dictionaryService.saveDictionary(mapToDictionary(file));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public DictionaryDto mapToDictionary(MultipartFile file) throws IOException {
        // Using Jdk8Module for mapping to Optional types
        val mapper = new ObjectMapper().registerModule(new Jdk8Module());
        val dictionary = mapper.readValue(file.getBytes(), DictionaryDto.class);
        System.out.println(dictionary.name());
        return dictionary;
    }

}
