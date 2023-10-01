package mikaa.config;

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

  static final String PLAYER_STATE = "player-state";

}
