package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.dto.v2.mapper.DictionaryMapper;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.ExpressionDetails;
import org.lekitech.gafalag.entity.v2.ExpressionMatchDetails;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpressionServiceV2 {

    private final DictionaryMapper mapper;
    private final ExpressionRepositoryV2 expressionRepo;

    public Page<String> searchSuggestions(String exp, String srcLang, Pageable pageable) {
        return expressionRepo.fuzzySearchByExpressionAndSrcLang(exp, srcLang, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ExpressionResponseDto> findExpressionsBySpellingAndSrcLang(String spelling,
                                                                           String srcLang,
                                                                           String distLang,
                                                                           Pageable pageable) {
        final Page<Expression> entities = expressionRepo
                .fuzzySearchBySpellingAndSrcLang(spelling, srcLang, pageable);

        final List<ExpressionResponseDto> dtos = entities.stream().map(expression -> {
                    final List<ExpressionMatchDetails> matchDetails = expression.getExpressionMatchDetails();
                    final List<ExpressionDetails> expressionDetails = matchDetails.stream()
                            .map(expMatchDetails -> {
                                val expDetail = expMatchDetails.getExpressionDetails();
                                val filteredDefinitionDetailsByDistLang = expDetail.getDefinitionDetails()
                                        .stream().filter(
                                                defDetail -> defDetail.getLanguage().getId().equals(distLang)
                                        ).toList();
                                expDetail.setDefinitionDetails(filteredDefinitionDetailsByDistLang);
                                return expDetail;
                            }).toList();
                    return mapper.toDto(expression.getSpelling(), expressionDetails);
                }
        ).toList();

        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }

}
