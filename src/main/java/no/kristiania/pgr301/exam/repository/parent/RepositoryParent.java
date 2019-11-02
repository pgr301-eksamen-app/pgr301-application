package no.kristiania.pgr301.exam.repository.parent;

import no.kristiania.pgr301.exam.aop.logging.db.LogDbCall;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

import static no.kristiania.pgr301.exam.aop.logging.db.LogDbCall.Type.INSERT;
import static no.kristiania.pgr301.exam.aop.logging.db.LogDbCall.Type.SELECT;

@NoRepositoryBean
public interface RepositoryParent<T, ID> extends JpaRepository<T, ID> {

  @NonNull
  @LogDbCall(type = SELECT)
  Optional<T> findById(@NonNull ID id);

  @NonNull
  @LogDbCall(type = INSERT)
  <S extends T> S save(@NonNull S s);

  @NonNull
  @LogDbCall(type = SELECT)
  List<T> findAll(@NonNull Sort sort);

}
