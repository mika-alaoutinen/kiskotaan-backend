package mikaa.events.holes;

import mikaa.kiskotaan.domain.HolePayload;

public interface HoleProducer {

  void holeAdded(HolePayload payload);

  void holeUpdated(HolePayload payload);

  void holeDeleted(HolePayload payload);

}
