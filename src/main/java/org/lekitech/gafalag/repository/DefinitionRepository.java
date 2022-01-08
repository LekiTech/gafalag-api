package org.lekitech.gafalag.repository;

import org.lekitech.gafalag.entity.Definition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DefinitionRepository extends JpaRepository<Definition, UUID> {
}
