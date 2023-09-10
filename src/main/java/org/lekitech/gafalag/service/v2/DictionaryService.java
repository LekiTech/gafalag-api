package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.DictionaryDto;
import org.lekitech.gafalag.dto.v2.ExpressionDto;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.Source;
import org.lekitech.gafalag.entity.v2.WrittenSource;
import org.lekitech.gafalag.repository.v2.DictionaryRepository;
import org.lekitech.gafalag.repository.v2.LanguageRepositoryV2;
import org.lekitech.gafalag.repository.v2.SourceRepositoryV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final SourceRepositoryV2 sourceRepositoryV2;
    private final LanguageRepositoryV2 languageRepositoryV2;

    public void save(ExpressionDto dto) {
        dictionaryRepository.save(new Expression());
    }

    public void saveDictionary(DictionaryDto dto) {


        val source = sourceRepositoryV2.save(new Source(Source.WRITTEN));

        val writtenSource = new WrittenSource(
                source,
                dto.name(),
                dto.authors(),
                dto.publicationYear(),
                dto.providedBy(),
                dto.providedByURL(),
                dto.processedBy(),
                dto.copyright(),
                dto.seeSourceURL(),
                dto.description()
        );


        val expLang = languageRepositoryV2.getById(dto.expressionLanguageId());
        val defLang = languageRepositoryV2.getById(dto.definitionLanguageId());

        for (val expression : dto.expressions()) {

            for (val expDetail : expression.details()) {

                for (val defDetail : expDetail.definitionDetails()) {

                    for (val definition : defDetail.definitions()) {

                        for (val definitionTag : definition.tags()) {

                        }
                    }

                    for (val defDetailExample : defDetail.examples()) {

                        for (val defDetailExampleTag : defDetailExample.tags()) {

                        }

                    }

                    for (val defDetailTag : defDetail.tags()) {

                    }


                }
            }
        }

        val expressions = dto.expressions()
                .stream()
                .map(expression -> new Expression())
                .toList();

        dictionaryRepository.saveAll(expressions);
    }

}