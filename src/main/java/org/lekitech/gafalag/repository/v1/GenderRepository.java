package org.lekitech.gafalag.repository.v1;

import org.lekitech.gafalag.entity.v1.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("transactionManagerV1")
public interface GenderRepository extends JpaRepository<Gender, Long> {
}
