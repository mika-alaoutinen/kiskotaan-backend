package mikaa.producers.score;

import mikaa.feature.score.ScoreEntity;

public interface ScoreProducer {

  void scoreAdded(ScoreEntity entity);

  void scoreDeleted(ScoreEntity entity);

}
