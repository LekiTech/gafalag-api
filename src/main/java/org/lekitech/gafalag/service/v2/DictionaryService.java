package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.DefinitionDetailsDto;
import org.lekitech.gafalag.dto.v2.DictionaryDto;
import org.lekitech.gafalag.dto.v2.ExampleDto;
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

        val source = sourceRepositoryV2.save(new Source(Source.Type.WRITTEN));

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
                        source
                );

                if (expDetail.examples().isPresent()) {
                    List<ExpressionExample> expressionExampleEntities = expDetail.examples().get()
                            .stream()
                            .map(exampleDto -> new ExpressionExample(
                                    expressionDetailsEntity,
                                    new Example(exampleDto.src(), exampleDto.trl(), expLang, defLang, exampleDto.raw())
                            )).toList();
                    expressionDetailsEntity.addExpressionExamples(expressionExampleEntities);
                }

                ExpressionMatchDetails expressionMatchDetailsEntity = new ExpressionMatchDetails(expressionEntity, expressionDetailsEntity);
                expressionDetailsEntity.addExpressionMatchDetails(List.of(expressionMatchDetailsEntity));

                expressionDetailsEntities.add(expressionDetailsEntity);

                List<DefinitionDetails> definitionDetailsEntities = new ArrayList<>();
                for (val defDetail : expDetail.definitionDetails()) {

                    DefinitionDetails definitionDetailEntity = new DefinitionDetails(expressionDetailsEntity, defLang);

                    List<Definition> definitionEntities = createDefinitionList(defDetail, definitionDetailEntity);
                    definitionDetailEntity.addDefinitions(definitionEntities);
                    definitionDetailsEntities.add(definitionDetailEntity);

                    if (defDetail.examples().isPresent()) {
                        List<DefinitionExample> definitionExampleList = createDefinitionExampleList(
                                defDetail.examples().get(), definitionDetailEntity, expLang, defLang
                        );
                        definitionDetailEntity.addDefinitionExamples(definitionExampleList);
                    }

                    if (defDetail.tags().isPresent()) {
                        List<DefinitionDetailsTag> definitionDetailsTagEntities = createDefinitionDetailsTags(
                                defDetail.tags().get(), definitionDetailEntity
                        );
                        definitionDetailEntity.addDefinitionDetailsTags(definitionDetailsTagEntities);
                    }


                }

                expressionDetailsEntity.addDefinitionDetails(definitionDetailsEntities);
            }
            expressionEntity.setExpressionMatchDetails(
                    expressionDetailsEntities.stream()
                            .map(expressionDetails -> new ExpressionMatchDetails(expressionEntity, expressionDetails))
                            .toList()
            );
            expressionEntities.add(expressionEntity);
        }

        dictionaryRepository.saveAll(expressionEntities);
    }

    private List<DefinitionDetailsTag> createDefinitionDetailsTags(List<String> tags, DefinitionDetails definitionDetailEntity) {
        List<DefinitionDetailsTag> definitionDetailsTagEntities = new ArrayList<>();
        for (val defDetailTag : tags) {
            Tag tagEntity = new Tag(defDetailTag);
            DefinitionDetailsTag definitionDetailsTagEntity = new DefinitionDetailsTag(tagEntity, definitionDetailEntity);
            definitionDetailsTagEntities.add(definitionDetailsTagEntity);
        }
        return definitionDetailsTagEntities;
    }

    private List<DefinitionExample> createDefinitionExampleList(List<ExampleDto> examples,
                                                                DefinitionDetails definitionDetailsEntity,
                                                                Language expLang,
                                                                Language defLang) {
        List<DefinitionExample> definitionExampleList = new ArrayList<>();
        for (val defDetailExample : examples) {
            Example exampleEntity = new Example(
                    defDetailExample.src(),
                    defDetailExample.trl(),
                    expLang,
                    defLang,
                    defDetailExample.raw()
            );
            if (defDetailExample.tags().isPresent()) {
                List<ExampleTag> exampleTagEntities = new ArrayList<>();
                for (val defDetailExampleTag : defDetailExample.tags().get()) {
                    Tag tagEntity = new Tag(defDetailExampleTag);
                    val exampleTagEntity = new ExampleTag(tagEntity, exampleEntity);
                    exampleTagEntities.add(exampleTagEntity);
                }
                exampleEntity.addExampleTags(exampleTagEntities);
            }
            val definitionExample = new DefinitionExample(exampleEntity, definitionDetailsEntity);
            definitionExampleList.add(definitionExample);
        }
        return definitionExampleList;
    }

    private List<Definition> createDefinitionList(DefinitionDetailsDto defDetail, DefinitionDetails definitionDetailEntity) {
        List<Definition> definitionEntities = new ArrayList<>();
        for (val definition : defDetail.definitions()) {

            Definition definitionEntity = new Definition(definition.value(), definitionDetailEntity);
            if (definition.tags().isPresent()) {
                val definitionTagList = createDefinitionTagList(definition.tags().get(), definitionEntity);
                definitionEntity.setDefinitionTags(definitionTagList);
            }
            definitionEntities.add(definitionEntity);
        }
        return definitionEntities;
    }

    private List<DefinitionTag> createDefinitionTagList(List<String> definitionTags, Definition definitionEntity) {
        List<DefinitionTag> definitionTagEntities = new ArrayList<>();
        for (val definitionTag : definitionTags) {
            Tag tagEntity = new Tag(definitionTag);
            DefinitionTag definitionTagEntity = new DefinitionTag(tagEntity, definitionEntity);
            definitionTagEntities.add(definitionTagEntity);
        }
        return definitionTagEntities;
    }


}