package org.lekitech.gafalag.repository.specification;

import lombok.NonNull;
import org.lekitech.gafalag.entity.Expression;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ExpressionSpecifications implements Specification<Expression> {

    private final List<Specification<Expression>> specifications = new ArrayList<>(3);

    @Override
    public Predicate toPredicate(@NonNull Root<Expression> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder builder) {
        return builder.and(
                specifications.stream()
                        .map(expSpec -> expSpec.toPredicate(root, query, builder))
                        .toArray(Predicate[]::new)
        );
    }

    public ExpressionSpecifications with(Specification<Expression> specification) {
        this.specifications.add(specification);
        return this;
    }
}
