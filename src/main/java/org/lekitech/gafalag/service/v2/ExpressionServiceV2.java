package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.ExpressionAndSimilar;
import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.dto.v2.SimilarDto;
import org.lekitech.gafalag.dto.v2.mapper.DictionaryMapper;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The `ExpressionServiceV2` class provides methods to interact with expressions and their details
 * in a version 2 of the application. It handles operations like searching for expressions, finding
 * expression suggestions, and retrieving expression details.
 * <p>
 * This service class is responsible for managing the business logic related to expressions and
 * acts as an intermediary between the data layer and the presentation layer.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExpressionServiceV2 {

    /* Dependencies */
    private final DictionaryMapper mapper;
    private final ExpressionRepositoryV2 expressionRepo;

    /**
     * Retrieves a page of search suggestions based on the provided expression, source language,
     * and pageable information.
     *
     * @param spelling The expression to search for.
     * @param expLang  The source language for the expression.
     * @param size     The limit of the suggestions.
     * @return A List of strings containing search suggestions.
     */
    public List<SimilarDto> searchSuggestions(String spelling, String expLang, String defLang, Integer size) {
        final List<Expression> expressions =
                expressionRepo.fuzzySearchSpellingsListBySpellingAndExpLang(spelling, expLang, defLang, size);
        return mapper.toDto(expressions);
    }

    @Transactional
    public ExpressionAndSimilar getExpressionByIdAndSimilar(UUID id, String defLang, Integer size) {
        final Optional<Expression> expOptional = expressionRepo.findById(id);
        if (expOptional.isPresent()) {
            final Expression expression = expOptional.get();
            final List<ExpressionDetails> expressionDetails = expression.getExpressionDetails()
                    .stream().map(expDetails -> {
                        val filteredDefinitionDetails = expDetails.getDefinitionDetails().stream().filter(
                                definitionDetails -> definitionDetails.getLanguage().getId().equals(defLang)
                        ).toList();
                        expDetails.setDefinitionDetails(filteredDefinitionDetails);
                        return expDetails;
                    }).toList();
            final ExpressionResponseDto expressionResponseDto = mapper.toDto(expression.getSpelling(), expressionDetails);
            final List<SimilarDto> similarDtos = searchSuggestions(expression.getSpelling(), expression.getLanguage().getId(), defLang, size);
            return new ExpressionAndSimilar(expressionResponseDto, similarDtos);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public ExpressionResponseDto getExpressionBySpellingAndExpLangAndDefLang(String spelling, String expLang, String defLang) {
        final Optional<Expression> expOptional = expressionRepo.findExpressionBySpellingAndLanguageAndDefLanguage(spelling, expLang, defLang);
        if (expOptional.isPresent()) {
            final Expression expression = expOptional.get();
            final List<ExpressionDetails> expressionDetails = expression.getExpressionDetails()
                    .stream().map(expDetails -> {
                        val filteredDefinitionDetails = expDetails.getDefinitionDetails().stream().filter(
                                definitionDetails -> definitionDetails.getLanguage().getId().equals(defLang)
                        ).toList();
                        expDetails.setDefinitionDetails(filteredDefinitionDetails);
                        return expDetails;
                    }).toList();
            final ExpressionResponseDto expressionResponseDto = mapper.toDto(expression.getSpelling(), expressionDetails);
            return expressionResponseDto;
        } else {
            throw new IllegalArgumentException();
        }
    }
}