package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.ExpressionAndSuggestions;
import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.dto.v2.mapper.DictionaryMapper;
import org.lekitech.gafalag.entity.v2.DefinitionDetails;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.stereotype.Service;

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
    public List<String> searchSuggestions(String spelling, String expLang, String defLang, Long size) {
        return expressionRepo.fuzzySearchSpellingsListBySpellingAndExpLang(spelling, expLang, defLang, size);
    }

    public ExpressionAndSuggestions getExpressionByIdAndSuggestions(UUID id, String defLang, Long size) {
        final Optional<Expression> expOptional = expressionRepo.findById(id);
        if (expOptional.isPresent()) {
            final Expression expression = expOptional.get();
            final List<DefinitionDetails> details =
            expression.getExpressionDetails().stream()
                    .map(expDetail ->
                      expDetail.getDefinitionDetails().stream()
                               .filter(defDetail -> defDetail.getLanguage().getId().equals(defLang))
                    );
            final ExpressionResponseDto expressionResponseDto = expression.map(expression -> {
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
            return null;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
