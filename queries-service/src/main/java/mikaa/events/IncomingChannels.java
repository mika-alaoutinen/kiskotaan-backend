package mikaa.events;

public interface IncomingChannels {

  interface Course {
    static final String COURSE_ADDED = "course-added";
    static final String COURSE_DELETED = "course-deleted";
    static final String COURSE_UPDATED = "course-updated";
  }

  interface Hole {
    static final String HOLE_ADDED = "hole-added";
    static final String HOLE_DELETED = "hole-deleted";
    static final String HOLE_UPDATED = "hole-updated";
  }

  interface Player {
    static final String PLAYER_ADDED = "player-added";
    static final String PLAYER_DELETED = "player-deleted";
    static final String PLAYER_UPDATED = "player-updated";
  }

  interface Score {
    static final String SCORE_ADDED = "score-added";
    static final String SCORE_DELETED = "score-deleted";
  }

  interface ScoreCard {
    static final String SCORECARD_ADDED = "scorecard-added";
    static final String SCORECARD_DELETED = "scorecard-deleted";
  }

}
