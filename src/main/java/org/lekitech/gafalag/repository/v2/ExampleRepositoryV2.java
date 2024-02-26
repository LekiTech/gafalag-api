package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Example;
import org.lekitech.gafalag.projection.ExampleProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExampleRepositoryV2 extends JpaRepository<Example, UUID> {

    /**
     * Executes a native SQL query to find expressions and examples based on the provided search string and language, with pagination.
     * This method performs a search for expressions and examples matching the given search string and expression language.
     * And it's also possible to filter by tag.
     *
     * @param searchString The string to search for within example raw text.
     * @param exLang       The language of the 'example source' or 'example translation'.
     * @param pageable     Pagination information for the query results.
     * @param tag          The tag to search for (default is null).
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
                   CAST(ex.id as varchar)  AS "exampleId",
                   ex."source"             AS "source",
                   ex."translation"        AS "translation",
                   ex.src_lang_id          AS "srcLangId",
                   ex.trl_lang_id          AS "trlLangId",
                   ex.raw                  AS "raw",
                   et.tag_abbr             AS "exampleTags"
            FROM example ex
                     LEFT JOIN example_tag et ON ex.id = et.example_id
                     JOIN definition_example de ON ex.id = de.example_id
                     JOIN definition_details dd ON de.definition_details_id = dd.id
                     JOIN expression_match_details emd ON dd.expression_details_id = emd.expression_details_id
                     JOIN expression exp ON emd.expression_id = exp.id
            WHERE (:tag IS NULL OR et.tag_abbr = CAST(:tag AS varchar))
              AND (ex.src_lang_id = :exLang OR ex.trl_lang_id = :exLang)
              AND (to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
                OR ex."raw" ILIKE '%' || :searchString || '%')
                        
            UNION
                        
            SELECT CAST(exp.id as varchar) AS "expressionId",
                   exp.spelling            AS "expressionSpelling",
                   CAST(ex.id as varchar)  AS "exampleId",
                   ex."source"             AS "source",
                   ex."translation"        AS "translation",
                   ex.src_lang_id          AS "srcLangId",
                   ex.trl_lang_id          AS "trlLangId",
                   ex.raw                  AS "raw",
                   et.tag_abbr             AS "exampleTags"
            FROM example ex
                     LEFT JOIN example_tag et ON ex.id = et.example_id
                     JOIN expression_example ee ON ex.id = ee.example_id
                     JOIN expression_match_details emd ON ee.expression_details_id = emd.expression_details_id
                     JOIN expression exp ON emd.expression_id = exp.id
            WHERE (:tag IS NULL OR et.tag_abbr = CAST(:tag AS varchar))
              AND (ex.src_lang_id = :exLang OR ex.trl_lang_id = :exLang)
              AND (to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
                OR ex."raw" ILIKE '%' || :searchString || '%')
            ORDER BY "expressionSpelling"
            """,
            countQuery = """
                    SELECT count(*)
                    FROM (SELECT CAST(exp.id as varchar) AS "expressionId",
                                 exp.spelling            AS "expressionSpelling",
                                 CAST(ex.id as varchar)  AS "exampleId",
                                 ex."source"             AS "source",
                                 ex."translation"        AS "translation",
                                 ex.src_lang_id          AS "srcLangId",
                                 ex.trl_lang_id          AS "trlLangId",
                                 ex.raw                  AS "raw",
                                 et.tag_abbr             AS "exampleTags"
                          FROM example ex
                                   LEFT JOIN example_tag et ON ex.id = et.example_id
                                   JOIN definition_example de ON ex.id = de.example_id
                                   JOIN definition_details dd ON de.definition_details_id = dd.id
                                   JOIN expression_match_details emd ON dd.expression_details_id = emd.expression_details_id
                                   JOIN expression exp ON emd.expression_id = exp.id
                          WHERE (:tag IS NULL OR et.tag_abbr = CAST(:tag AS varchar))
                            AND (ex.src_lang_id = :exLang OR ex.trl_lang_id = :exLang)
                            AND (to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
                              OR ex."raw" ILIKE '%' || :searchString || '%')
                                        
                          UNION
                                        
                          SELECT CAST(exp.id as varchar) AS "expressionId",
                                 exp.spelling            AS "expressionSpelling",
                                 CAST(ex.id as varchar)  AS "exampleId",
                                 ex."source"             AS "source",
                                 ex."translation"        AS "translation",
                                 ex.src_lang_id          AS "srcLangId",
                                 ex.trl_lang_id          AS "trlLangId",
                                 ex.raw                  AS "raw",
                                 et.tag_abbr             AS "exampleTags"
                          FROM example ex
                                   LEFT JOIN example_tag et ON ex.id = et.example_id
                                   JOIN expression_example ee ON ex.id = ee.example_id
                                   JOIN expression_match_details emd ON ee.expression_details_id = emd.expression_details_id
                                   JOIN expression exp ON emd.expression_id = exp.id
                          WHERE (:tag IS NULL OR et.tag_abbr = CAST(:tag AS varchar))
                            AND (ex.src_lang_id = :exLang OR ex.trl_lang_id = :exLang)
                            AND (to_tsvector('simple', ex."raw") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
                              OR ex."raw" ILIKE '%' || :searchString || '%')) AS result
                    """,
            nativeQuery = true)
    Page<ExampleProjection> findExpressionAndExample(@NonNull @Param("searchString") String searchString,
                                                     @NonNull @Param("exLang") String exLang,
                                                     Pageable pageable,
                                                     @Nullable @Param("tag") String tag);
}
