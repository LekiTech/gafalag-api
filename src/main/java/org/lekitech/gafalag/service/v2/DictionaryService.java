package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.*;
import org.lekitech.gafalag.entity.v2.*;
import org.lekitech.gafalag.repository.v2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryService {

    private final ExpressionRepositoryV2 expressionRepositoryV2;
    private final ExpressionMatchDetailsRepository expressionMatchDetailsRepository;
    private final WrittenSourceRepository writtenSourceRepository;
    private final SourceRepositoryV2 sourceRepositoryV2;
    private final LanguageRepositoryV2 languageRepositoryV2;
    private final TagRepository tagRepositoryV2;

    public void save(ExpressionDto dto) {
        expressionRepositoryV2.save(new Expression());
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
        final List<Expression> expressionEntities = new ArrayList<>();
        for (ExpressionDto expressionDto : dto.expressions()) {
            val optionalExpression = expressionRepositoryV2.findBySpellingAndLanguage(expressionDto.spelling(), expLang);
            val expressionDetailsEntities = createExpressionDetails(source, expLang, defLang, expressionDto);
            if (optionalExpression.isPresent()) {
                val expressionEntity = optionalExpression.get();
                val expressionMatchDetailsEntities = expressionDetailsEntities.stream().map(
                        expressionDetails -> new ExpressionMatchDetails(expressionEntity, expressionDetails)
                ).toList();
                expressionMatchDetailsRepository.saveAll(expressionMatchDetailsEntities);
            } else {
                val expressionEntity = new Expression(expressionDto.spelling(), expLang);
                val expressionMatchDetailsEntities = expressionDetailsEntities.stream().map(
                        expressionDetails -> new ExpressionMatchDetails(expressionEntity, expressionDetails)
                ).toList();
                if (expressionEntity.getExpressionMatchDetails().isEmpty()) {
                    expressionEntity.setExpressionMatchDetails(expressionMatchDetailsEntities);
                } else {
                    expressionEntity.getExpressionMatchDetails().addAll(expressionMatchDetailsEntities);
                }
                expressionEntity.setExpressionMatchDetails(expressionMatchDetailsEntities);
                expressionEntities.add(expressionEntity);
            }
        }
        expressionRepositoryV2.saveAll(expressionEntities);
    }

    private List<ExpressionDetails> createExpressionDetails(Source source,
                                                            Language expLang,
                                                            Language defLang,
                                                            ExpressionDto expressionDto) {
        final List<ExpressionDetails> expressionDetailsEntities = new ArrayList<>();
        for (ExpressionDetailsDto expressionDetailsDto : expressionDto.details()) {
            final ExpressionDetails expressionDetailsEntity = new ExpressionDetails(
                    expressionDetailsDto.gr(),
                    expressionDetailsDto.inflection(),
                    source
            );
            if (expressionDetailsDto.examples().isPresent()) {
                final List<ExpressionExample> expressionExampleEntities = createExpressionExampleList(
                        expressionDetailsDto.examples().get(), expressionDetailsEntity, expLang, defLang
                );
                expressionDetailsEntity.addExpressionExamples(expressionExampleEntities);
            }
            final List<DefinitionDetails> definitionDetailsEntities = new ArrayList<>();
            for (DefinitionDetailsDto definitionDetailsDto : expressionDetailsDto.definitionDetails()) {
                final DefinitionDetails definitionDetailEntity = createDefinitionDetails(
                        expLang, defLang, expressionDetailsEntity, definitionDetailsDto
                );
                definitionDetailsEntities.add(definitionDetailEntity);
            }
            expressionDetailsEntity.addDefinitionDetails(definitionDetailsEntities);
            expressionDetailsEntities.add(expressionDetailsEntity);
        }
        return expressionDetailsEntities;
    }

    private DefinitionDetails createDefinitionDetails(Language expLang,
                                                      Language defLang,
                                                      ExpressionDetails expressionDetailsEntity,
                                                      DefinitionDetailsDto defDetail) {
        final DefinitionDetails definitionDetailEntity = new DefinitionDetails(expressionDetailsEntity, defLang);
        final List<Definition> definitionEntities = createDefinitionList(defDetail, definitionDetailEntity);
        definitionDetailEntity.addDefinitions(definitionEntities);
        if (defDetail.examples().isPresent()) {
            final List<DefinitionExample> definitionExampleList = createDefinitionExampleList(
                    defDetail.examples().get(), definitionDetailEntity, expLang, defLang
            );
            definitionDetailEntity.addDefinitionExamples(definitionExampleList);
        }
        if (defDetail.tags().isPresent()) {
            final List<DefinitionDetailsTag> definitionDetailsTagEntities = createDefinitionDetailsTags(
                    defDetail.tags().get(), definitionDetailEntity
            );
            definitionDetailEntity.addDefinitionDetailsTags(definitionDetailsTagEntities);
        }
        return definitionDetailEntity;
    }

    private List<DefinitionDetailsTag> createDefinitionDetailsTags(List<String> tags,
                                                                   DefinitionDetails definitionDetailEntity) {
        final List<DefinitionDetailsTag> definitionDetailsTagEntities = new ArrayList<>();
        for (String defDetailTag : tags) {
            final Tag tagEntity = getOrCreateTag(defDetailTag);
            final DefinitionDetailsTag definitionDetailsTagEntity = new DefinitionDetailsTag(
                    tagEntity, definitionDetailEntity
            );
            definitionDetailsTagEntities.add(definitionDetailsTagEntity);
        }
        return definitionDetailsTagEntities;
    }

    private List<ExpressionExample> createExpressionExampleList(List<ExampleDto> examples,
                                                                ExpressionDetails expressionDetailsEntity,
                                                                Language expLang,
                                                                Language defLang) {
        final List<ExpressionExample> expressionExampleList = new ArrayList<>();
        for (ExampleDto exampleDto : examples) {
            final Example exampleEntity = createExample(expLang, defLang, exampleDto);
            final ExpressionExample expressionExample = new ExpressionExample(expressionDetailsEntity, exampleEntity);
            expressionExampleList.add(expressionExample);
        }
        return expressionExampleList;
    }

    private List<DefinitionExample> createDefinitionExampleList(List<ExampleDto> examples,
                                                                DefinitionDetails definitionDetailsEntity,
                                                                Language expLang,
                                                                Language defLang) {
        final List<DefinitionExample> definitionExampleList = new ArrayList<>();
        for (ExampleDto exampleDto : examples) {
            final Example exampleEntity = createExample(expLang, defLang, exampleDto);
            final DefinitionExample definitionExample = new DefinitionExample(exampleEntity, definitionDetailsEntity);
            definitionExampleList.add(definitionExample);
        }
        return definitionExampleList;
    }

    private Example createExample(Language expLang, Language defLang, ExampleDto exampleDto) {
        final Example exampleEntity = new Example(
                exampleDto.src(),
                exampleDto.trl(),
                expLang,
                defLang,
                exampleDto.raw()
        );
        if (exampleDto.tags().isPresent()) {
            final List<ExampleTag> exampleTagEntities = createExampleTags(exampleDto.tags().get(), exampleEntity);
            exampleEntity.addExampleTags(exampleTagEntities);
        }
        return exampleEntity;
    }

    private List<ExampleTag> createExampleTags(List<String> tagsDto, Example exampleEntity) {
        final List<ExampleTag> exampleTagEntities = new ArrayList<>();
        for (val defDetailExampleTag : tagsDto) {
            final Tag tagEntity = getOrCreateTag(defDetailExampleTag);
            final ExampleTag exampleTagEntity = new ExampleTag(tagEntity, exampleEntity);
            exampleTagEntities.add(exampleTagEntity);
        }
        return exampleTagEntities;
    }

    private List<Definition> createDefinitionList(DefinitionDetailsDto defDetail, DefinitionDetails definitionDetailEntity) {
        final List<Definition> definitionEntities = new ArrayList<>();
        for (val definition : defDetail.definitions()) {
            final Definition definitionEntity = new Definition(definition.value(), definitionDetailEntity);
            if (definition.tags().isPresent()) {
                final List<DefinitionTag> definitionTagList = createDefinitionTagList(
                        definition.tags().get(), definitionEntity
                );
                definitionEntity.addDefinitionTags(definitionTagList);
            }
            definitionEntities.add(definitionEntity);
        }
        return definitionEntities;
    }

    private List<DefinitionTag> createDefinitionTagList(List<String> definitionTags, Definition definitionEntity) {
        final List<DefinitionTag> definitionTagEntities = new ArrayList<>();
        for (String definitionTag : definitionTags) {
            final Tag tagEntity = getOrCreateTag(definitionTag);
            final DefinitionTag definitionTagEntity = new DefinitionTag(tagEntity, definitionEntity);
            definitionTagEntities.add(definitionTagEntity);
        }
        return definitionTagEntities;
    }

    private Tag getOrCreateTag(String tagAbbreviation) {
        final Optional<Tag> foundTag = tagRepositoryV2.findById(tagAbbreviation);
        return foundTag.orElseGet(() -> tagRepositoryV2.save(new Tag(tagAbbreviation)));
    }
}
