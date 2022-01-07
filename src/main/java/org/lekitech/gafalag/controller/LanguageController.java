package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.DialectRequest;
import org.lekitech.gafalag.dto.LanguageRequest;
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

    @GetMapping(path = "")
    public List<Language> getAllLanguages() {
        var langs = languageService.getAll();
        return langs;
    }

    @PostMapping(path = "")
    public void saveLanguage(@RequestBody LanguageRequest dto) {
         languageService.save(new Language(dto.name(), dto.iso639_2(), dto.iso639_3()));
    }

    @PostMapping(path = "/dialect")
    public void saveDialect(@RequestBody DialectRequest dto) {
        var language = languageService.getById(dto.languageId());
        dialectService.save(new Dialect(dto.name(), language));
    }
}
