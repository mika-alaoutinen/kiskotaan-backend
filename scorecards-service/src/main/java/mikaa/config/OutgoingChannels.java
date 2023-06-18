package mikaa.config;

public interface OutgoingChannels {

  interface Score {
    static final String SCORE_ADDED = "score-added";
    static final String SCORE_DELETED = "score-deleted";
  }

  interface ScoreCard {
    static final String SCORECARD_ADDED = "scorecard-added";
    static final String SCORECARD_DELETED = "scorecard-deleted";
    static final String SCORECARD_STATE = "scorecard-details";
  }

}
