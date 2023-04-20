package mikaa.events.scorecard;

public interface ScoreCardProducer {

  void scoreCardAdded(ScoreCardPayload payload);

  void scoreCardDeleted(ScoreCardPayload payload);

}
