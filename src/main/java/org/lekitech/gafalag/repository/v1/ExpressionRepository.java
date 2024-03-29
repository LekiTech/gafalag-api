package org.lekitech.gafalag.repository.v1;

import lombok.NonNull;
import org.lekitech.gafalag.entity.v1.Expression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional("transactionManagerV1")
public interface ExpressionRepository extends JpaRepository<Expression, UUID> {

    @NonNull Page<Expression> findAll(@NonNull Pageable pageable);

    Page<Expression> findAllByLanguageId(String languageId, Pageable pageable);

    // TODO: find way to filter on "toLang" from database query to replace current implementation in ExpressionService
    //       We need tis filter for the cases when we have multiple translations form single language e.g.:
    //          - Russian -> Lezgi
    //          - Russian -> Tabasaran
    //       In this case user should only get translations from Russian to the selected language and
    //       not all available languages
    @Query(nativeQuery = true,
            value = "SELECT * FROM expression e " +
                    "WHERE e.language_id = :fromLang " +
                    "ORDER BY spelling <-> :exp " +
                    "LIMIT 10")
    List<Expression> fuzzySearch(@Param("exp") String exp,
                                 @Param("fromLang") String fromLang);

    @Query(nativeQuery = true,
            value = "SELECT * FROM expression e " +
                    "JOIN definition d ON e.id = d.expression_id " +
                    "WHERE e.language_id = :toLang AND d.language_id = :fromLang " +
                        "AND to_tsvector(d.definition_text) @@ to_tsquery(:text) " +
                    "LIMIT 10")
    List<Expression> fullTextSearch(@Param("text") String text,
                                    @Param("fromLang") String fromLang,
                                    @Param("toLang") String toLang);
}
