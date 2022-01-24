package org.lekitech.gafalag.repository;

import lombok.NonNull;
import org.lekitech.gafalag.entity.Expression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, UUID> {

    @NonNull Page<Expression> findAll(@NonNull Pageable pageable);

    Page<Expression> findAllByLanguageId(String languageId, Pageable pageable);
}
