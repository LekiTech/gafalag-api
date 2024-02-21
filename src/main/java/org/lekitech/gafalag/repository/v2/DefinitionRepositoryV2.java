package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Definition;
import org.lekitech.gafalag.projection.DefinitionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DefinitionRepositoryV2 extends JpaRepository<Definition, UUID> {

    /**
     * Executes a native SQL query to find expressions and their associated definitions based on the provided search string
     * and language, with pagination. This method performs a search for expressions and their definitions matching
     * the given search string and expression language.
     *
     * @param searchString The string to search for within definition values.
     * @param expLang      The language of the expressions to search within.
     * @param pageable     Pagination information for the query results.
     * @return a {@link Page} containing {@link DefinitionProjection} objects representing the expressions
     * and their associated definitions found.
     * @throws IllegalArgumentException if the searchString or expLang parameter is null.
     *                                  This method constructs a native SQL query that searches for expressions and their associated definitions based on the provided
     *                                  search string and expression language. It searches for definitions where the value matches the search string or contains the
     *                                  search string as a substring. The search is performed using case-insensitive comparison. The results are paginated according
     *                                  to the provided {@link Pageable} object. Each page contains {@link DefinitionProjection} objects representing the expressions
     *                                  and their associated definitions found, including the expression ID, spelling, definition ID, value, and tags.
     */
    @Query(value = """
            SELECT CAST(expr.id AS varchar) AS "expressionId",
                   expr.spelling            AS "expressionSpelling",
                   CAST(d.id as varchar)    AS "definitionId",
                   d.value                  AS "definitionValue",
                   dt.tag_abbr              AS "definitionTags"
            FROM expression expr
                     JOIN expression_match_details emd ON emd.expression_id = expr.id
                     JOIN expression_details ed ON ed.id = emd.expression_details_id
                     JOIN definition_details dd ON dd.expression_details_id = ed.id
                     JOIN definition d ON d.definition_details_id = dd.id
                     LEFT JOIN definition_tag dt ON dt.definition_id = d.id
            WHERE expr.language_id = :expLang
                AND to_tsvector('simple', d."value") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
               OR d."value" ILIKE '%' || :searchString || '%'
            ORDER BY spelling
            """,
            countQuery = """
                    SELECT count(*)
                    FROM expression expr
                             JOIN expression_match_details emd ON emd.expression_id = expr.id
                             JOIN expression_details ed ON ed.id = emd.expression_details_id
                             JOIN definition_details dd ON dd.expression_details_id = ed.id
                             JOIN definition d ON d.definition_details_id = dd.id
                             LEFT JOIN definition_tag dt ON dt.definition_id = d.id
                    WHERE expr.language_id = :expLang
                        AND to_tsvector('simple', d."value") @@ to_tsquery('simple', replace(:searchString, ' ', ' & ') || ':*')
                       OR d."value" ILIKE '%' || :searchString || '%'
                    """,
            nativeQuery = true)
    Page<DefinitionProjection> findExpressionsAndDefinitions(@NonNull @Param("searchString") String searchString,
                                                             @NonNull @Param("expLang") String expLang,
                                                             Pageable pageable);
}
