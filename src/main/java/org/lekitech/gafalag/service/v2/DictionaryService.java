package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.DictionaryDto;
import org.lekitech.gafalag.dto.v2.ExpressionDto;
import org.lekitech.gafalag.entity.v2.*;
import org.lekitech.gafalag.repository.v2.DictionaryRepository;
import org.lekitech.gafalag.repository.v2.LanguageRepositoryV2;
import org.lekitech.gafalag.repository.v2.SourceRepositoryV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
            List<ExpressionDetails> expressionDetailsEntities = new ArrayList<>();
            for (val expDetail : expression.details()) {

                ExpressionDetails expressionDetailsEntity = new ExpressionDetails(
                        expDetail.gr(),
                        expDetail.inflection(),
                        source,
                        null // TODO: 9/11/23
                );
                List<ExpressionExample> expressionExampleEntities = expDetail.examples().stream()
                        .map(exampleDto -> new ExpressionExample(
                                expressionDetailsEntity,
                                new Example(exampleDto.src(), exampleDto.trl(), expLang, defLang, exampleDto.raw())
                        )).toList();
                expressionDetailsEntity.addExpressionExamples(expressionExampleEntities);

                expressionDetailsEntities.add(expressionDetailsEntity);


                for (val defDetail : expDetail.definitionDetails()) {

                    DefinitionDetails definitionDetailEntity = new DefinitionDetails(expressionDetailsEntity, defLang); // TODO: 9/10/23
                    List<Definition> definitionEntities = new ArrayList<>();
                    for (val definition : defDetail.definitions()) {

                        Definition definitionEntity = new Definition(definition.value());
                        List<DefinitionTag> definitionTagEntities = new ArrayList<>();
                        for (val definitionTag : definition.tags()) {
                            Tag tagEntity = new Tag(definitionTag);
                            DefinitionTag definitionTagEntity = new DefinitionTag(tagEntity, definitionEntity);
                            definitionTagEntities.add(definitionTagEntity);
                        }
                        definitionEntities.add(definitionEntity);
                    }

                    List<Example> exampleEntities = new ArrayList<>();
                    for (val defDetailExample : defDetail.examples()) {
                        Example exampleEntity = new Example(
                                defDetailExample.src(),
                                defDetailExample.trl(),
                                expLang,
                                defLang,
                                defDetailExample.raw()
                        );
                        List<ExampleTag> exampleTagEntities = new ArrayList<>();
                        for (val defDetailExampleTag : defDetailExample.tags()) {
                            Tag tagEntity = new Tag(defDetailExampleTag);
                            exampleTagEntities.add(new ExampleTag(tagEntity, exampleEntity));
                        }
                        exampleEntities.add(exampleEntity);
                    }

                    List<DefinitionDetailsTag> definitionDetailsTagEntities = new ArrayList<>();
                    for (val defDetailTag : defDetail.tags()) {
                        Tag tagEntity = new Tag(defDetailTag);
                        definitionDetailsTagEntities.add(new DefinitionDetailsTag(tagEntity, definitionDetailEntity));
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