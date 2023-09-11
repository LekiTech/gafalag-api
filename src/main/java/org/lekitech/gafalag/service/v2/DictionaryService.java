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
import org.lekitech.gafalag.repository.v2.WrittenSourceRepository;
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
    private final WrittenSourceRepository writtenSourceRepository;
    private final SourceRepositoryV2 sourceRepositoryV2;
    private final LanguageRepositoryV2 languageRepositoryV2;

    public void save(ExpressionDto dto) {
        dictionaryRepository.save(new Expression());
    }

    @Transactional
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
        writtenSourceRepository.save(writtenSource);

        val expLang = languageRepositoryV2.getById(dto.expressionLanguageId());
        val defLang = languageRepositoryV2.getById(dto.definitionLanguageId());

        List<Expression> expressionEntities = new ArrayList<>();
        for (val expression : dto.expressions()) {

            Expression expressionEntity = new Expression(expression.spelling(), expLang);

            List<ExpressionDetails> expressionDetailsEntities = new ArrayList<>();

            for (val expDetail : expression.details()) {

                ExpressionDetails expressionDetailsEntity = new ExpressionDetails(
                        expDetail.gr(),
                        expDetail.inflection(),
                        source);

                if (expDetail.examples() != null) {
                    List<ExpressionExample> expressionExampleEntities = expDetail.examples().stream()
                            .map(exampleDto -> new ExpressionExample(
                                    expressionDetailsEntity,
                                    new Example(exampleDto.src(), exampleDto.trl(), expLang, defLang, exampleDto.raw())
                            )).toList();
                    expressionDetailsEntity.addExpressionExamples(expressionExampleEntities);
                }

                ExpressionMatchDetails expressionMatchDetailsEntity = new ExpressionMatchDetails(expressionEntity, expressionDetailsEntity);
                expressionDetailsEntity.addExpressionMatchDetails(List.of(expressionMatchDetailsEntity));


                expressionDetailsEntities.add(expressionDetailsEntity);


                for (val defDetail : expDetail.definitionDetails()) {

                    DefinitionDetails definitionDetailEntity = new DefinitionDetails(expressionDetailsEntity, defLang); // TODO: 9/10/23
                    List<Definition> definitionEntities = new ArrayList<>();
                    for (val definition : defDetail.definitions()) {

                        Definition definitionEntity = new Definition(definition.value());

                        if (definition.tags() != null) {
                            List<DefinitionTag> definitionTagEntities = new ArrayList<>();
                            for (val definitionTag : definition.tags()) {
                                Tag tagEntity = new Tag(definitionTag);
                                DefinitionTag definitionTagEntity = new DefinitionTag(tagEntity, definitionEntity);
                                definitionTagEntities.add(definitionTagEntity);
                            }
                        }

                        definitionEntities.add(definitionEntity);
                    }

                    if (defDetail.examples() != null) {
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
                            if (defDetailExample.tags() != null) {
                                for (val defDetailExampleTag : defDetailExample.tags()) {
                                    Tag tagEntity = new Tag(defDetailExampleTag);
                                    exampleTagEntities.add(new ExampleTag(tagEntity, exampleEntity));
                                }
                            }
                            exampleEntities.add(exampleEntity);
                        }
                    }

                    if (defDetail.tags() != null) {
                        List<DefinitionDetailsTag> definitionDetailsTagEntities = new ArrayList<>();
                        for (val defDetailTag : defDetail.tags()) {
                            Tag tagEntity = new Tag(defDetailTag);
                            definitionDetailsTagEntities.add(new DefinitionDetailsTag(tagEntity, definitionDetailEntity));
                        }
                    }


                }
            }
            expressionEntities.add(expressionEntity);
        }

        val expressions = dto.expressions()
                .stream()
                .map(expression -> new Expression())
                .toList();

        dictionaryRepository.saveAll(expressions);
    }

}