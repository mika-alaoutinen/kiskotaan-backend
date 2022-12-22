package mikaa.feature;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardService {

  private final ScoreCardRepository repository;

  List<ScoreCardEntity> findAll() {
    return repository.listAll();
  }

  Optional<ScoreCardEntity> findOne(long id) {
    return repository.findByIdOptional(id);
  }

}
