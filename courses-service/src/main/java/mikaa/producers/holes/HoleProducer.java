package mikaa.producers.holes;

import mikaa.domain.Hole;

public interface HoleProducer {

  void holeAdded(Hole hole, long courseId);

  void holeUpdated(Hole hole, long courseId);

  void holeDeleted(Hole hole, long courseId);

}
