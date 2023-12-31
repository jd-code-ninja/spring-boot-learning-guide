package org.learning.guide.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonSummaryRepository extends CrudRepository<PersonSummaryEntity, String> {
}
