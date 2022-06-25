package org.lekitech.gafalag.repository;

import lombok.NonNull;
import org.lekitech.gafalag.entity.Expression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, UUID>, JpaSpecificationExecutor<Expression> {

    @NonNull Page<Expression> findAll(@NonNull Pageable pageable);

    Page<Expression> findAllByLanguageId(String languageId, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM expression " +
                    "ORDER BY spelling <-> :exp " +
                    "LIMIT 10")
    List<Expression> fuzzySearch(@Param("exp") String exp);

    @Query(nativeQuery = true,
            value = "SELECT * FROM expression e " +
                    "JOIN definition d ON e.id = d.expression_id " +
                    "WHERE to_tsvector(d.definition_text) @@ to_tsquery(:text) " +
                    "LIMIT 10")
    List<Expression> fullTextSearch(@Param("text") String text);
}
