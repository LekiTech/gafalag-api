package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional("transactionManagerV2")
public interface ExpressionRepositoryV2 extends JpaRepository<Expression, UUID> {

    List<Expression> findAllByLanguage(@NonNull Language language);

    @Query(value = """
            SELECT *
            FROM expression e
            WHERE e.language_id = :srcLang
            ORDER BY spelling <-> :spelling
            """,
            countQuery = """
                    SELECT count(*)
                    FROM expression e
                    WHERE e.language_id = :srcLang
                    """,
            nativeQuery = true)
    Page<Expression> fuzzySearchBySpellingAndSrcLang(@NonNull @Param("spelling") String spelling,
                                                     @NonNull @Param("srcLang") String srcLang,
                                                     Pageable pageable);

    @Query(value = """
            SELECT spelling
            FROM expression e
            WHERE e.language_id = :srcLang
            ORDER BY spelling <-> :exp
            """,
            countQuery = """
                    SELECT count(*)
                    FROM expression e
                    WHERE e.language_id = :srcLang
                    """,
            nativeQuery = true)
    Page<String> fuzzySearchByExpressionAndSrcLang(@NonNull @Param("exp") String exp,
                                                   @NonNull @Param("srcLang") String srcLang,
                                                   Pageable pageable);

}
