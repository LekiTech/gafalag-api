package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Example;
import org.lekitech.gafalag.projection.ExampleProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExampleRepositoryV2 extends JpaRepository<Example, UUID> {

    /**
     * Executes a native SQL query to find expressions and examples based on the provided search string and language, with pagination.
     * This method performs a search for expressions and examples matching the given search string and expression language.
     *
     * @param searchString The string to search for within example raw text.
     * @param expLang      The language of the expressions to search within.
     * @param pageable     Pagination information for the query results.
     * @return a {@link Page} containing {@link ExampleProjection} objects representing the expressions and examples found.
     * @throws IllegalArgumentException if the searchString or expLang parameter is null.
     *                                  This method constructs a native SQL query that searches for expressions and examples based on the provided search string and
     *                                  expression language. It searches for expressions and examples where the raw text matches the search string or contains the
     *                                  search string as a substring. The search is performed using case-insensitive comparison. The results are paginated according
     *                                  to the provided {@link Pageable} object. Each page contains {@link ExampleProjection} objects representing the expressions and examples found.
     */
    @Query(value = """
            SELECT CAST(exp.id as varchar) AS "expressionId",
                   exp.spelling            AS "expressionSpelling",
                   CAST(ex.id as varchar)  AS id,
                   ex."source"             AS "source",
                   ex."translation"        AS "translation",
                   ex.src_lang_id          AS "srcLangId",
                   ex.trl_lang_id          AS "trlLangId",
                   ex.raw                  AS raw
            FROM example ex
                     JOIN definition_example de ON ex.id = de.example_id
                     JOIN definition_details dd ON de.definition_details_id = dd.id
                     JOIN expression_match_details emd ON dd.expression_details_id = emd.expression_details_id
                     JOIN expression exp ON emd.expression_id = exp.id
            WHERE to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
               OR ex."raw" ILIKE '%' || :searchString || '%'
                        
            UNION
            -- Second Path: example -> expression_example -> expression_match_details -> expression
            SELECT CAST(exp.id as varchar) AS "expressionId",
                   exp.spelling            AS "expressionSpelling",
                   CAST(ex.id as varchar)  AS id,
                   ex."source"             AS "source",
                   ex."translation"        AS "translation",
                   ex.src_lang_id          AS "srcLangId",
                   ex.trl_lang_id          AS "trlLangId",
                   ex.raw                  AS raw
            FROM example ex
                     JOIN expression_example ee ON ex.id = ee.example_id
                     JOIN expression_match_details emd ON ee.expression_details_id = emd.expression_details_id
                     JOIN expression exp ON emd.expression_id = exp.id 
            WHERE language_id = :expLang -- todo exLang (в javadoc объяснить что значит exLang) если ищу русское слово, то либо ex.src_lang_id должен быть 'rus', либо ex.trl_lang_id = 'rus'
                AND to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
               OR ex."raw" ILIKE '%' || :searchString || '%'
            ORDER BY "expressionSpelling"
            """,
            countQuery = """
                    SELECT count(*)
                    FROM (SELECT exp.spelling AS "expressionSpelling"
                          FROM example ex
                                   JOIN definition_example de ON ex.id = de.example_id
                                   JOIN definition_details dd ON de.definition_details_id = dd.id
                                   JOIN expression_match_details emd ON dd.expression_details_id = emd.expression_details_id
                                   JOIN expression exp ON emd.expression_id = exp.id
                          WHERE to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
                             OR ex."raw" ILIKE '%' || :searchString || '%'
                          UNION
                          SELECT exp.spelling AS "expressionSpelling"
                          FROM example ex
                                   JOIN expression_example ee ON ex.id = ee.example_id
                                   JOIN expression_match_details emd ON ee.expression_details_id = emd.expression_details_id
                                   JOIN expression exp ON emd.expression_id = exp.id
                          WHERE language_id = :expLang
                              AND to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
                             OR ex."raw" ILIKE '%' || :searchString || '%') AS result
                    """,
            nativeQuery = true)
    Page<ExampleProjection> findExpressionAndExample(@NonNull @Param("searchString") String searchString,
                                                     @NonNull @Param("expLang") String expLang,
                                                     Pageable pageable);
}
