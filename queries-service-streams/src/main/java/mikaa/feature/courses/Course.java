package mikaa.feature.courses;

import mikaa.kiskotaan.courses.CoursePayload;
import mikaa.kiskotaan.domain.Hole;

public record Course(long id, String name, int par) {

  public static Course fromPayload(CoursePayload payload) {
    return new Course(payload.getId(), payload.getName(), calculateCoursePar(payload));
  }

  private static int calculateCoursePar(CoursePayload payload) {
    return payload.getHoles().stream().mapToInt(Hole::getPar).sum();
  }

}
