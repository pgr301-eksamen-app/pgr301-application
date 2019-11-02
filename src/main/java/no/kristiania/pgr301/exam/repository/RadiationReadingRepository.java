package no.kristiania.pgr301.exam.repository;

import no.kristiania.pgr301.exam.model.RadiationReading;
import no.kristiania.pgr301.exam.repository.parent.RepositoryParent;
import org.springframework.stereotype.Repository;

@Repository
public interface RadiationReadingRepository extends RepositoryParent<RadiationReading, Long> {}
