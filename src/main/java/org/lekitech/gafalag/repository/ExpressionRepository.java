package org.lekitech.gafalag.repository;

import lombok.NonNull;
import org.lekitech.gafalag.entity.Expression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, UUID> {

    @NonNull Page<Expression> findAll(@NonNull Pageable pageable);

    Page<Expression> findAllByLanguageId(Long languageId, Pageable pageable);

    Page<Expression> findAllByLanguage_Iso3(String language_iso3, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM expression " +
                    "ORDER BY spelling <-> :exp " +
                    "LIMIT 10")
    List<Expression> fuzzySearch(@Param("exp") String exp);
}
