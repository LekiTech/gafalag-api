package org.lekitech.gafalag.repository.v2;

import org.lekitech.gafalag.entity.v2.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional("transactionManagerV2")
public interface SourceRepositoryV2 extends JpaRepository<Source, UUID> {
}
