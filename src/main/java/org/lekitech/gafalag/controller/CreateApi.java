package org.lekitech.gafalag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.*;
import org.lekitech.gafalag.entity.*;
import org.lekitech.gafalag.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/gafalag-api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreateApi {

    private final ExpressionService expressionService;
    private final DialectService dialectService;
    private final LanguageService languageService;

    @PostMapping(path = "/expression")
    public Response saveExpression(@RequestBody ExpressionDto dto) {
        val language = languageService.getById(dto.getLanguageId());
        val dialect = dialectService.findById(dto.getDialectId());
        val exp = expressionService
                .save(new Expression(
                        dto.getSpelling(),
                        dto.getMisspelling(),
                        dto.getInflection(),
                        dto.getGender(),
                        language,
                        dialect
                ));
        return Response.of(
                exp.getSpelling(),
                exp.getMisspelling(),
                exp.getInflection(),
                exp.getGender(),
                new LanguageDto(language),
                dialect.getName()
        );
    }

    @PostMapping(path = "/language")
    public void saveLanguage(@RequestBody LanguageDto dto) {
        languageService.save(new Language(dto.getName(), dto.getIso639_3(), dto.getIso639_2()));
    }

    @PostMapping(path = "/dialect")
    public void saveDialect(@RequestBody DialectDto dto) {
        val language = languageService.getById(dto.getLanguageId());
        dialectService.save(new Dialect(dto.getName(), language));
    }
}
