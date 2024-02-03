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
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional("transactionManagerV2")
public interface ExpressionRepositoryV2 extends JpaRepository<Expression, UUID> {

    List<Expression> findAllByLanguage(@NonNull Language language);

    /*
     * Filtering here not only on expression language but also on a definition language to get the actual suggestions that user would expect
     * `EXISTS` subquery stops evaluating once it finds a match, rather than joining all matching rows, so we get better performance
     */
    @Query(value = """
            SELECT *
            FROM expression e
            WHERE e.language_id = :expLang
            AND EXISTS (
               SELECT 1
               FROM expression_match_details emd
               JOIN expression_details ed ON emd.expression_details_id = ed.id
               JOIN definition_details dd ON ed.id = dd.expression_details_id
               WHERE emd.expression_id = e.id
               AND dd.language_id = :defLang
            )
            ORDER BY e.spelling <-> :spelling
            LIMIT :size
            """,
            nativeQuery = true)
    List<Expression> fuzzySearchSpellingsListBySpellingAndExpLang(
            @NonNull @Param("spelling") String spelling,
            @NonNull @Param("expLang") String expLang,
            @NonNull @Param("defLang") String defLang,
            Integer size
    );

    @Query(value = """
            SELECT *
            FROM expression e
            WHERE e.spelling ILIKE :spelling
            AND e.language_id = :expLang
            AND EXISTS (
               SELECT 1
               FROM expression_match_details emd
               JOIN expression_details ed ON emd.expression_details_id = ed.id
               JOIN definition_details dd ON ed.id = dd.expression_details_id
               WHERE emd.expression_id = e.id
               AND dd.language_id = :defLang
            )
            """,
            nativeQuery = true)
    Optional<Expression> findExpressionBySpellingAndLanguageAndDefLanguage(
            @NonNull @Param("spelling") String spelling,
            @NonNull @Param("expLang") String expLang,
            @NonNull @Param("defLang") String defLang
    );
}
