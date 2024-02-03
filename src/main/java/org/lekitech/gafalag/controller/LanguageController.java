package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.dialect.*;
import org.lekitech.gafalag.dto.language.*;
import org.lekitech.gafalag.service.DialectService;
import org.lekitech.gafalag.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/language")
@RequiredArgsConstructor
public class LanguageController {

    private final DialectService dialectService;
    private final LanguageService languageService;
    private final LanguageMapper languageMapper;
    private final DialectMapper dialectMapper;

    @GetMapping(path = "")
    public List<LanguageResponse> getAllLanguages() {
        return languageService.getAll().stream().map(languageMapper::toDto).toList();
    }

    @PostMapping(path = "")
    public LanguageResponse saveLanguage(@RequestBody LanguageRequest request) {
        return languageMapper.toDto(languageService.save(languageMapper.toEntity(request)));
    }

    @PostMapping(path = "/dialect")
    public DialectResponse saveDialect(@RequestBody DialectRequest request) {
        return dialectMapper.toDto(dialectService.save(dialectMapper.toEntity(request)));
    }
}
