package org.lekitech.gafalag.repository.v1;

import org.lekitech.gafalag.entity.v1.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional("transactionManagerV1")
public interface SourceRepository extends JpaRepository<Source, UUID> {
    Optional<Source> findByName(String name);
}