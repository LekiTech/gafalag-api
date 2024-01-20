package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.dto.v2.mapper.DictionaryMapper;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * @param srcLang  The source language for the expression.
     * @param size     The limit of the suggestions.
     * @return A List of strings containing search suggestions.
     */
    public List<String> searchSuggestions(String spelling, String srcLang, Long size) {
        return expressionRepo.fuzzySearchSpellingsListBySpellingAndSrcLang(spelling, srcLang, size);
    }

    /**
     * Finds expressions by spelling, source language, and destination language with fuzzy matching,
     * and returns a Page of ExpressionResponseDto objects.
     *
     * @param spelling The spelling of the expression to search for.
     * @param srcLang  The source language for the expression.
     * @param distLang The destination language for the expression details.
     * @param pageable The pageable information for the search results.
     * @return A Page of ExpressionResponseDto objects containing expression details.
     */
    @Deprecated
    @Transactional(readOnly = true)
    public Page<ExpressionResponseDto> findExpressionsBySpellingAndSrcLang(String spelling,
                                                                           String srcLang,
                                                                           String distLang,
                                                                           Pageable pageable) {
        final Page<Expression> entities = expressionRepo
                .fuzzySearchSExpressionsListBySpellingAndSrcLang(spelling, srcLang, pageable);

        final List<ExpressionResponseDto> dtos = entities.stream().map(expression -> {
            final List<ExpressionDetails> expressionDetails = expression.getExpressionDetails().stream()
                    .map(expDetail -> {
                        val filteredDefinitionDetailsByDistLang = expDetail.getDefinitionDetails()
                                .stream().filter(
                                        defDetail -> defDetail.getLanguage().getId().equals(distLang)
                                ).toList();
                        expDetail.setDefinitionDetails(filteredDefinitionDetailsByDistLang);
                        return expDetail;
                    }).toList();
            return mapper.toDto(expression.getSpelling(), expressionDetails);
        }).toList();

        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }
}
