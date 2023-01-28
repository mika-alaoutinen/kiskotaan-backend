package mikaa.feature.course;

public interface CourseFinder {

  CourseEntity findOrThrow(long id);

}
