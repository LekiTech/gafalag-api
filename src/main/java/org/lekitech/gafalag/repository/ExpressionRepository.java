package org.lekitech.gafalag.repository;

import org.lekitech.gafalag.entity.Expression;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, UUID> {
    Page<Expression> findAll(Pageable pageable);
    Page<Expression> findAllByLanguageId(Long languageId, Pageable pageable);
}
