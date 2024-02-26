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

import java.sql.Date;
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
            AND (
                EXISTS (
                -- Search for matches of `defLang` in `DefinitionDetails`
                   SELECT 1
                   FROM expression_match_details emd
                   JOIN expression_details ed ON emd.expression_details_id = ed.id
                   JOIN definition_details dd ON ed.id = dd.expression_details_id
                   WHERE emd.expression_id = e.id
                   AND dd.language_id = :defLang
                )
                OR EXISTS (
                -- Search for matches of `defLang` to "translation language" in `Example` through `ExpressionExample` relation
                   SELECT 1
                   FROM expression_example ee
                   JOIN example ex ON ee.example_id = ex.id
                   JOIN expression_details ed ON ee.expression_details_id = ed.id
                   JOIN expression_match_details emd ON ed.id = emd.expression_details_id
                   WHERE emd.expression_id = e.id
                   AND ex.trl_lang_id = :defLang
                )
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


    /**
     * Executes a native SQL query to find an expression by its spelling, expression language, and definition language.
     *
     * @param spelling The spelling of the expression to search for (case-insensitive).
     * @param expLang  The language code for expressions to search within.
     * @param defLang  The language code for definition details or translation language to match against.
     * @return an Optional containing the Expression object if found, or empty if not found.
     * @throws IllegalArgumentException if any of the parameters (spelling, expLang, defLang) is null.
     *                                  This method constructs a native SQL query that searches for an expression based on the provided spelling, language, and target
     *                                  definition language. It performs a case-insensitive search for the expression spelling. It then filters expressions based on
     *                                  whether they have matching definition details in the specified language or examples with translation language matching the
     *                                  specified language. If a matching expression is found, it is returned wrapped in an Optional; otherwise, an empty Optional is returned.
     */
    @Query(value = """
            SELECT *
            FROM expression e
            -- ILIKE is used for case insensitive search
            WHERE e.spelling ILIKE :spelling
            AND e.language_id = :expLang
            AND (
                EXISTS (
                -- Search for matches of `defLang` in `DefinitionDetails`
                   SELECT 1
                   FROM expression_match_details emd
                   JOIN expression_details ed ON emd.expression_details_id = ed.id
                   JOIN definition_details dd ON ed.id = dd.expression_details_id
                   WHERE emd.expression_id = e.id
                   AND dd.language_id = :defLang
                )
                OR EXISTS (
                -- Search for matches of `defLang` to "translation language" in `Example` through `ExpressionExample` relation
                   SELECT 1
                   FROM expression_example ee
                   JOIN example ex ON ee.example_id = ex.id
                   JOIN expression_details ed ON ee.expression_details_id = ed.id
                   JOIN expression_match_details emd ON ed.id = emd.expression_details_id
                   WHERE emd.expression_id = e.id
                   AND ex.trl_lang_id = :defLang
                )
            )
            """,
            nativeQuery = true)
    Optional<Expression> findExpressionBySpellingAndLanguageAndDefLanguage(
            @NonNull @Param("spelling") String spelling,
            @NonNull @Param("expLang") String expLang,
            @NonNull @Param("defLang") String defLang
    );


    /**
     * Executes a native SQL query to find an expression based on the current date.
     * This method retrieves an expression in the Lezgi language based on the provided current date.
     * It orders the expressions by their creation date and retrieves the expression at the offset calculated
     * from the difference between the provided current date and a reference date (1930-04-27, which is used
     * as a reference point in this context).
     *
     * @param currentDate The current date to use for calculating the offset.
     * @return an Optional containing the Expression object if found, or empty if not found.
     * @throws IllegalArgumentException if the currentDate parameter is null.
     *                                  This method constructs a native SQL query that selects an expression in the Lezgi language based on the provided current date.
     *                                  It calculates an offset based on the difference between the provided current date and a reference date (1930-04-27).
     *                                  The expressions are ordered by their creation date, and the method retrieves the expression at the calculated offset.
     *                                  If a matching expression is found, it is returned wrapped in an Optional; otherwise, an empty Optional is returned.
     */
    @Query(value = """
            SELECT *
            FROM expression e
            WHERE e.language_id = 'lez'
            ORDER BY e.created_at
            LIMIT 1 OFFSET mod(
                -- CURRENT DATE - REFERENCE DATE (used Shtulski rebellion as reference)
                        :currentDate - DATE '1930-04-27',
                -- Count all Lezgi expressions to ensure that upper offset limit matches amount of expressions
                        (SELECT COUNT(*) FROM expression e WHERE e.language_id = 'lez')
                )
            """,
            nativeQuery = true)
    Optional<Expression> findExpressionByCurrentDate(@NonNull @Param("currentDate") Date currentDate);


    /**
     * Executes a native SQL query to find expressions based on a specific tag and expression language, with pagination.
     * This method performs a search for expressions that are associated with the provided tag in various levels
     * of the expression hierarchy.
     *
     * @param tag      The tag abbreviation to search for within expressions.
     * @param expLang  The language of the expressions to search within.
     * @param pageable Pagination information for the query results.
     * @return a {@link Page} containing {@link Expression} objects representing the expressions found.
     * @throws IllegalArgumentException if the tag or expLang parameter is null.
     *                                  This method constructs a native SQL query that searches for expressions based on the provided tag and expression language.
     *                                  It searches for expressions associated with the tag at different levels of the expression hierarchy, including within
     *                                  definition details, definitions, examples within definition details, and examples within expression details. The results
     *                                  are paginated according to the provided Pageable object. Each page contains {@link Expression} objects representing the expressions found.
     *                                  The expressions are ordered by their spelling.
     */
    @Query(value = """
            -- SELECT to search for a tag in definition_details.
            SELECT exp.*
            FROM expression exp
                     JOIN expression_match_details emd ON emd.expression_id = exp.id
                     JOIN definition_details dd ON dd.expression_details_id = emd.expression_details_id
                     JOIN definition_details_tag ddt ON dd.id = ddt.definition_details_id
            WHERE ddt.tag_abbr = :tag
              AND exp.language_id = :expLang
            UNION
            -- SELECT to search for a tag in definition.
            SELECT exp.*
            FROM expression exp
                     JOIN expression_match_details emd ON emd.expression_id = exp.id
                     JOIN definition_details dd ON dd.expression_details_id = emd.expression_details_id
                     JOIN definition d ON dd.id = d.definition_details_id
                     JOIN definition_tag dt ON d.id = dt.definition_id
            WHERE dt.tag_abbr = :tag
              AND exp.language_id = :expLang
            UNION
            -- SELECT to search for a tag in definition_example.
            SELECT exp.*
            FROM expression exp
                     JOIN expression_match_details emd ON emd.expression_id = exp.id
                     JOIN definition_details dd ON dd.expression_details_id = emd.expression_details_id
                     JOIN definition_example de ON dd.id = de.definition_details_id
                     JOIN example ex ON de.example_id = ex.id
                     JOIN example_tag et ON et.example_id = ex.id
            WHERE et.tag_abbr = :tag
              AND exp.language_id = :expLang
            UNION
            -- SELECT to search for a tag in expression_example.
            SELECT exp.*
            FROM expression exp
                     JOIN expression_match_details emd ON emd.expression_id = exp.id
                     JOIN expression_example ee ON ee.expression_details_id = emd.expression_details_id
                     JOIN example ex ON ex.id = ee.example_id
                     JOIN example_tag et ON et.example_id = ex.id
            WHERE et.tag_abbr = :tag
              AND exp.language_id = :expLang
            ORDER BY spelling
            """,
            countQuery = """
                    SELECT count(*)
                    FROM (SELECT exp.*
                          FROM expression exp
                                   JOIN expression_match_details emd ON emd.expression_id = exp.id
                                   JOIN definition_details dd ON dd.expression_details_id = emd.expression_details_id
                                   JOIN definition_details_tag ddt ON dd.id = ddt.definition_details_id
                          WHERE ddt.tag_abbr = :tag
                            AND exp.language_id = :expLang
                          UNION
                          SELECT exp.*
                          FROM expression exp
                                   JOIN expression_match_details emd ON emd.expression_id = exp.id
                                   JOIN definition_details dd ON dd.expression_details_id = emd.expression_details_id
                                   JOIN definition d ON dd.id = d.definition_details_id
                                   JOIN definition_tag dt ON d.id = dt.definition_id
                          WHERE dt.tag_abbr = :tag
                            AND exp.language_id = :expLang
                          UNION
                          SELECT exp.*
                          FROM expression exp
                                   JOIN expression_match_details emd ON emd.expression_id = exp.id
                                   JOIN definition_details dd ON dd.expression_details_id = emd.expression_details_id
                                   JOIN definition_example de ON dd.id = de.definition_details_id
                                   JOIN example ex ON de.example_id = ex.id
                                   JOIN example_tag et ON et.example_id = ex.id
                          WHERE et.tag_abbr = :tag
                            AND exp.language_id = :expLang
                          UNION
                          SELECT exp.*
                          FROM expression exp
                                   JOIN expression_match_details emd ON emd.expression_id = exp.id
                                   JOIN expression_example ee ON ee.expression_details_id = emd.expression_details_id
                                   JOIN example ex ON ex.id = ee.example_id
                                   JOIN example_tag et ON et.example_id = ex.id
                          WHERE et.tag_abbr = :tag
                            AND exp.language_id = :expLang) AS result
                    """,
            nativeQuery = true)
    Page<Expression> findExpressionsByTagAndExpLang(@NonNull @Param("tag") String tag,
                                                    @NonNull @Param("expLang") String expLang,
                                                    Pageable pageable);
}
