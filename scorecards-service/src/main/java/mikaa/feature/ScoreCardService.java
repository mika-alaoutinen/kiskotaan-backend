package mikaa.feature;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class ScoreCardService {

  Optional<ScoreCardEntity> findOne(long id) {
    return Optional.empty();
  }

}
