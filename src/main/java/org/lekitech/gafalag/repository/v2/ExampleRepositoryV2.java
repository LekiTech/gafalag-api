package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExampleRepositoryV2 extends JpaRepository<Example, UUID> {

    @Query(value = """
            SELECT
                  CAST(expr.id as varchar) AS "expressionId",
                  expr.spelling AS "expressionSpelling",
                  CAST(ex.id as varchar) AS id,
                  ex."source" AS "source",
                  ex."translation" AS "translation",
                  ex.src_lang_id AS "srcLangId",
                  ex.trl_lang_id AS "trlLangId",
                  ex.raw AS raw
              FROM example ex
               JOIN definition_example de ON ex.id = de.example_id
               JOIN definition_details dd ON de.definition_details_id = dd.id
               JOIN expression_match_details emd ON dd.expression_details_id = emd.expression_details_id
               JOIN expression expr ON emd.expression_id = expr.id
               WHERE ex.source ILIKE CONCAT('%', :exp, '%')
               
              UNION
              
              -- Second Path: example -> expression_example -> expression_match_details -> expression
              SELECT
                  CAST(expr.id as varchar) AS "expressionId",
                  expr.spelling AS "expressionSpelling",
                  CAST(ex.id as varchar) AS id,
                  ex."source" AS "source",
                  ex."translation" AS "translation",
                  ex.src_lang_id AS "srcLangId",
                  ex.trl_lang_id AS "trlLangId",
                  ex.raw AS raw
              FROM example ex
               JOIN expression_example ee ON ex.id = ee.example_id
               JOIN expression_match_details emd ON ee.expression_details_id = emd.expression_details_id
               JOIN expression expr ON emd.expression_id = expr.id
               WHERE ex.source ILIKE CONCAT('%', :exp, '%')
            """,
            nativeQuery = true)
    List<ExampleProjection> findExpressionAndExample(@NonNull @Param("exp") String exp);
}
