package mikaa.feature.course;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.api.CoursesApi;
import mikaa.model.CourseDTO;
import mikaa.model.CourseSummaryDTO;

@ApplicationScoped
@RequiredArgsConstructor
class CourseResource implements CoursesApi {

  @Override
  public CourseDTO getCourse(Integer id) {
    throw new UnsupportedOperationException("Unimplemented method 'getCourse'");
  }

  @Override
  public List<CourseSummaryDTO> getCourses(
      String name,
      Integer holesMin,
      Integer holesMax,
      Integer parMin,
      Integer parMax) {
    throw new UnsupportedOperationException("Unimplemented method 'getCourses'");
  }

}
