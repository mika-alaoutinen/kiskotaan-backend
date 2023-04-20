package mikaa.events.score;

public interface ScoreProducer {

  void scoreAdded(ScorePayload payload);

  void scoreDeleted(ScorePayload payload);

}
