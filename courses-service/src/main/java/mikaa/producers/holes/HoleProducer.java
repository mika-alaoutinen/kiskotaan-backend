package mikaa.producers.holes;

import mikaa.kiskotaan.course.HolePayload;

public interface HoleProducer {

  void holeAdded(HolePayload payload);

  void holeUpdated(HolePayload payload);

  void holeDeleted(HolePayload payload);

}
