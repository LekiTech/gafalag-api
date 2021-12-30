package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.DialectDto;
import org.lekitech.gafalag.dto.LanguageDto;
import org.lekitech.gafalag.entity.Dialect;
import org.lekitech.gafalag.entity.Language;
import org.lekitech.gafalag.service.DialectService;
import org.lekitech.gafalag.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/language")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LanguageController {
    private final DialectService dialectService;
    private final LanguageService languageService;

    @GetMapping(path = "/")
    public List<Language> getAllLanguages() {
        var langs = languageService.getAll();
        return langs;
    }

    @PostMapping(path = "/")
    public void saveLanguage(@RequestBody LanguageDto dto) {
        languageService.save(new Language(dto.getName(), dto.getIso639_2(), dto.getIso639_3()));
    }

    @PostMapping(path = "/dialect")
    public void saveDialect(@RequestBody DialectDto dto) {
        var language = languageService.getById(dto.getLanguageId());
        dialectService.save(new Dialect(dto.getName(), language));
    }
}
