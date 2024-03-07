package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.*;
import org.lekitech.gafalag.dto.v2.mapper.SourceMapperV2;
import org.lekitech.gafalag.entity.v2.Definition;
import org.lekitech.gafalag.entity.v2.DefinitionDetails;
import org.lekitech.gafalag.entity.v2.DefinitionDetailsTag;
import org.lekitech.gafalag.entity.v2.DefinitionExample;
import org.lekitech.gafalag.entity.v2.DefinitionTag;
import org.lekitech.gafalag.entity.v2.Example;
import org.lekitech.gafalag.entity.v2.ExampleTag;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.lekitech.gafalag.entity.v2.ExpressionExample;
import org.lekitech.gafalag.entity.v2.Language;
import org.lekitech.gafalag.entity.v2.Source;
import org.lekitech.gafalag.entity.v2.Tag;
import org.lekitech.gafalag.entity.v2.WrittenSource;
import org.lekitech.gafalag.repository.v2.ExpressionDetailsRepositoryV2;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.lekitech.gafalag.repository.v2.LanguageRepositoryV2;
import org.lekitech.gafalag.repository.v2.SourceRepositoryV2;
import org.lekitech.gafalag.repository.v2.TagRepository;
import org.lekitech.gafalag.repository.v2.WrittenSourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final ExpressionRepositoryV2 expressionRepositoryV2;
    private final ExpressionDetailsRepositoryV2 expressionDetailsRepositoryV2;
    private final WrittenSourceRepository writtenSourceRepository;
    private final SourceMapperV2 sourceMapper;
    private final SourceRepositoryV2 sourceRepositoryV2;
    private final LanguageRepositoryV2 languageRepositoryV2;
    private final TagRepository tagRepositoryV2;

    public List<WrittenSourceDto> getAllWrittenSources() {
        return writtenSourceRepository.findAll().stream().map(ws ->sourceMapper.mapToDto(ws)).toList();
    }

    @Transactional
    public void saveDictionary(DictionaryDto dto) {
        try {
            final Source source = createAndSaveSource(dto);

            saveAllExpressionData(dto, source);
        } catch (Exception e) {
            log.error("Error saving dictionary data: {}", e.getMessage(), e);
        }
    }

    private Source createAndSaveSource(DictionaryDto dto) {
        final Source source = sourceRepositoryV2.save(new Source(Source.Type.WRITTEN));
        final WrittenSource writtenSource = new WrittenSource(
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
        return source;
    }

    public void saveAllExpressionData(DictionaryDto dto, Source source) {
        final Language expLang = languageRepositoryV2.getById(dto.expressionLanguageId());
        final Language defLang = languageRepositoryV2.getById(dto.definitionLanguageId());
        final Map<String, List<ExpressionDetailsDto>> expressionDtoMap = new HashMap<>();
        /* this map contains expressions from database that have already been stored */
        final Map<String, Expression> dbExpressionsMap = expressionRepositoryV2
                .findAllByLanguage(expLang).stream()
                .collect(Collectors.toMap(Expression::getSpelling, expression -> expression));

        for (final ExpressionDto expressionDto : dto.expressions()) {
            for (final String spelling : expressionDto.spelling()) {
                List<ExpressionDetailsDto> detailsList = expressionDtoMap.getOrDefault(spelling, new ArrayList<>());
                detailsList.addAll(expressionDto.details());
                expressionDtoMap.put(spelling, detailsList);
            }
        }

        expressionDtoMap.forEach(
                (spelling, details) -> saveSingleExpression(spelling, details, expLang, source, defLang, dbExpressionsMap)
        );
    }

    private void saveSingleExpression(String spelling,
                                      List<ExpressionDetailsDto> expressionDetailsDtoList,
                                      Language expLang,
                                      Source source,
                                      Language defLang,
                                      Map<String, Expression> dbExpressionsMap) {

        final Expression expressionEntity = dbExpressionsMap.containsKey(spelling)
                ? dbExpressionsMap.get(spelling)
                : expressionRepositoryV2.save(new Expression(spelling, expLang));

        expressionDetailsRepositoryV2.saveAll(
                createExpressionDetails(source, expLang, defLang, expressionDetailsDtoList, expressionEntity)
        );
    }

    private List<ExpressionDetails> createExpressionDetails(Source source,
                                                            Language expLang,
                                                            Language defLang,
                                                            List<ExpressionDetailsDto> expressionDetailsDtoList,
                                                            Expression expressionEntity) {
        final List<ExpressionDetails> expressionDetailsEntities = new ArrayList<>();

        for (final ExpressionDetailsDto expressionDetailsDto : expressionDetailsDtoList) {
            final ExpressionDetails expressionDetailsEntity = new ExpressionDetails(
                    expressionDetailsDto.gr(),
                    expressionDetailsDto.inflection(),
                    source
            );

            final List<ExampleDto> examples = expressionDetailsDto.examples();
            if (examples != null && !examples.isEmpty()) {
                final List<ExpressionExample> expressionExampleEntities = createExpressionExampleList(
                        examples, expressionDetailsEntity, expLang, defLang
                );
                expressionDetailsEntity.addExpressionExamples(expressionExampleEntities);
            }

            final List<DefinitionDetails> definitionDetailsEntities = new ArrayList<>();
            for (final DefinitionDetailsDto definitionDetailsDto : expressionDetailsDto.definitionDetails()) {
                final DefinitionDetails definitionDetailEntity = createDefinitionDetails(
                        expLang, defLang, expressionDetailsEntity, definitionDetailsDto
                );
                definitionDetailsEntities.add(definitionDetailEntity);
            }

            expressionDetailsEntity.addDefinitionDetails(definitionDetailsEntities);
            expressionDetailsEntities.add(expressionDetailsEntity);
            expressionEntity.getExpressionDetails().add(expressionDetailsEntity);
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

        final List<ExampleDto> examples = defDetail.examples();
        if (examples != null && !examples.isEmpty()) {
            final List<DefinitionExample> definitionExampleList = createDefinitionExampleList(
                    examples,
                    definitionDetailEntity,
                    expLang,
                    defLang
            );
            definitionDetailEntity.addDefinitionExamples(definitionExampleList);
        }

        final List<String> tags = defDetail.tags();
        if (tags != null && !tags.isEmpty()) {
            final List<DefinitionDetailsTag> definitionDetailsTagEntities = createDefinitionDetailsTags(
                    tags,
                    definitionDetailEntity
            );
            definitionDetailEntity.addDefinitionDetailsTags(definitionDetailsTagEntities);
        }

        return definitionDetailEntity;
    }

    private List<DefinitionDetailsTag> createDefinitionDetailsTags(List<String> tags,
                                                                   DefinitionDetails definitionDetailEntity) {
        final List<DefinitionDetailsTag> definitionDetailsTagEntities = new ArrayList<>();

        for (final String defDetailTag : tags) {
            final Tag tagEntity = getOrCreateTag(defDetailTag);
            final DefinitionDetailsTag definitionDetailsTagEntity = new DefinitionDetailsTag(
                    tagEntity,
                    definitionDetailEntity
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

        for (final ExampleDto exampleDto : examples) {
            final Example exampleEntity = createExample(
                    expLang,
                    defLang,
                    exampleDto
            );
            final ExpressionExample expressionExample = new ExpressionExample(
                    expressionDetailsEntity,
                    exampleEntity
            );
            expressionExampleList.add(expressionExample);
        }
        return expressionExampleList;
    }

    private List<DefinitionExample> createDefinitionExampleList(List<ExampleDto> examples,
                                                                DefinitionDetails definitionDetailsEntity,
                                                                Language expLang,
                                                                Language defLang) {
        final List<DefinitionExample> definitionExampleList = new ArrayList<>();

        for (final ExampleDto exampleDto : examples) {
            final Example exampleEntity = createExample(
                    expLang,
                    defLang,
                    exampleDto
            );
            final DefinitionExample definitionExample = new DefinitionExample(
                    exampleEntity,
                    definitionDetailsEntity
            );
            definitionExampleList.add(definitionExample);
        }
        return definitionExampleList;
    }

    private Example createExample(Language expLang,
                                  Language defLang,
                                  ExampleDto exampleDto) {
        final Example exampleEntity = new Example(
                exampleDto.src(),
                exampleDto.trl(),
                expLang,
                defLang,
                exampleDto.raw()
        );
        final List<String> tags = exampleDto.tags();
        if (tags != null && !tags.isEmpty()) {
            final List<ExampleTag> exampleTagEntities = createExampleTags(
                    tags,
                    exampleEntity
            );
            exampleEntity.addExampleTags(exampleTagEntities);
        }
        return exampleEntity;
    }

    private List<ExampleTag> createExampleTags(List<String> tagsDto,
                                               Example exampleEntity) {
        final List<ExampleTag> exampleTagEntities = new ArrayList<>();

        for (final String defDetailExampleTag : tagsDto) {
            final Tag tagEntity = getOrCreateTag(defDetailExampleTag);
            final ExampleTag exampleTagEntity = new ExampleTag(
                    tagEntity,
                    exampleEntity
            );
            exampleTagEntities.add(exampleTagEntity);
        }
        return exampleTagEntities;
    }

    private List<Definition> createDefinitionList(DefinitionDetailsDto defDetail,
                                                  DefinitionDetails definitionDetailEntity) {
        final List<Definition> definitionEntities = new ArrayList<>();

        for (final DefinitionDto definition : defDetail.definitions()) {
            final Definition definitionEntity = new Definition(
                    definition.value(),
                    definitionDetailEntity
            );
            final List<String> tags = definition.tags();
            if (tags != null && !tags.isEmpty()) {
                final List<DefinitionTag> definitionTagList = createDefinitionTagList(
                        tags,
                        definitionEntity
                );
                definitionEntity.addDefinitionTags(definitionTagList);
            }
            definitionEntities.add(definitionEntity);
        }
        return definitionEntities;
    }

    private List<DefinitionTag> createDefinitionTagList(List<String> definitionTags,
                                                        Definition definitionEntity) {
        final Set<DefinitionTag> definitionTagEntities = new HashSet<>();

        for (final String definitionTag : definitionTags) {
            final Tag tagEntity = getOrCreateTag(definitionTag);
            final DefinitionTag definitionTagEntity = new DefinitionTag(
                    tagEntity,
                    definitionEntity
            );
            definitionTagEntities.add(definitionTagEntity);
        }
        return definitionTagEntities.stream().toList();
    }

    private Tag getOrCreateTag(String tagAbbreviation) {
        final Optional<Tag> foundTag = tagRepositoryV2.findById(tagAbbreviation);
        return foundTag.orElseGet(() -> tagRepositoryV2.save(new Tag(tagAbbreviation)));
    }
}
