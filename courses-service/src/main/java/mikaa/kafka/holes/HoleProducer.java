package mikaa.kafka.holes;

public interface HoleProducer {

  void holeAdded(HolePayload payload);

  void holeUpdated(HolePayload payload);

  void holeDeleted(HolePayload payload);

}
