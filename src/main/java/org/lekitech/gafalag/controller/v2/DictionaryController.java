package org.lekitech.gafalag.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.DictionaryDto;
import org.lekitech.gafalag.dto.v2.WrittenSourceDto;
import org.lekitech.gafalag.service.v2.DictionaryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "v2/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/sources")
    public List<WrittenSourceDto> getAllWrittenSources(){
        return dictionaryService.getAllWrittenSources();
    }

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
        /* Using Jdk8Module for mapping to Optional types */
        val mapper = new ObjectMapper().registerModule(new Jdk8Module());
        return mapper.readValue(file.getBytes(), DictionaryDto.class);
    }

}
