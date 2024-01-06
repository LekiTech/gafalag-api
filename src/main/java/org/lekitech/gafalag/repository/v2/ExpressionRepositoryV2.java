package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Expression;
import org.lekitech.gafalag.entity.v2.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional("transactionManagerV2")
public interface ExpressionRepositoryV2 extends JpaRepository<Expression, UUID> {

    Optional<Expression> findBySpellingAndLanguage(@NonNull String spelling, @NonNull Language language);

    List<Expression> findAllByLanguage(@NonNull Language language);
}
