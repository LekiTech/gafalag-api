package org.lekitech.gafalag.dto.mapper;

import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Component
public class ReferenceMapper {

    @PersistenceContext
    private EntityManager entityManager;

    @ObjectFactory
    public <T> T map(@NonNull final Long id, @TargetType Class<T> type) {
        return entityManager.getReference(type, id);
    }

    @ObjectFactory
    public <T> T map(@NonNull final UUID id, @TargetType Class<T> type) {
        return entityManager.getReference(type, id);
    }

    @ObjectFactory
    public <T> T map(@NonNull final String id, @TargetType Class<T> type) {
        return entityManager.getReference(type, id);
    }
}