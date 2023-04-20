package mikaa.feature.scorecard;

public interface ScoreCardFinder {

  ScoreCardEntity findOrThrow(long id);

}
