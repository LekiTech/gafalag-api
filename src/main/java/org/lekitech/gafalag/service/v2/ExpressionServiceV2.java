package org.lekitech.gafalag.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.dto.v2.ExpressionResponseDto;
import org.lekitech.gafalag.dto.v2.mapper.DictionaryMapper;
import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.repository.v2.ExpressionRepositoryV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpressionServiceV2 {

    private final DictionaryMapper mapper;
    private final ExpressionRepositoryV2 expressionRepositoryV2;

    public Page<ExpressionResponseDto> findExpressionsBySpellingAndSrcLang(String spelling,
                                                                           String srcLang,
                                                                           Pageable pageable) {
        final Page<Expression> entities = expressionRepositoryV2
                .fuzzySearchBySpellingAndSrcLang(spelling, srcLang, pageable);

        final List<ExpressionResponseDto> dtos = entities.stream().map(expression ->
                mapper.toDto(expression.getSpelling(), null)
        ).toList();

        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }

    public Page<String> findSpellingsByExpressionAndSrcLang(String exp,
                                                            String srcLang,
                                                            Pageable pageable) {
        final Page<Expression> entities = expressionRepositoryV2
                .fuzzySearchBySpellingAndSrcLang(exp, srcLang, pageable);

        final List<String> dtos = entities.stream().map(Expression::getSpelling).toList();

        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }
}
