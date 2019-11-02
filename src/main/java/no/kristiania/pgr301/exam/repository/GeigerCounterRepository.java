package no.kristiania.pgr301.exam.repository;

import no.kristiania.pgr301.exam.model.GeigerCounter;
import no.kristiania.pgr301.exam.repository.parent.RepositoryParent;
import org.springframework.stereotype.Repository;

@Repository
public interface GeigerCounterRepository extends RepositoryParent<GeigerCounter, Long> {}
