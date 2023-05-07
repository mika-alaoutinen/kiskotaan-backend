package mikaa.feature.course;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.HoleDTO;

@ApplicationScoped
@Path("courses")
@RequiredArgsConstructor
public class CourseResource {

  private final CourseService service;

  @GET
  @Path("/{id}")
  public Uni<CourseDTO> getCourse(int id) {
    return service.getCourse(id).map(CourseResource::toCourse);
  }

  @GET
  public Multi<CourseSummaryDTO> getCourses(
      @RestQuery String name,
      @RestQuery Integer holesMin,
      @RestQuery Integer holesMax,
      @RestQuery Integer parMin,
      @RestQuery Integer parMax) {
    return service.getCourses().map(CourseResource::toSummary);
  }

  private static CourseDTO toCourse(Course course) {
    var holes = course.getHoles().stream().map(CourseResource::toHole).toList();

    return new CourseDTO(
        course.getExternalId(),
        course.getName(),
        calculateCoursePar(course),
        holes);
  }

  private static HoleDTO toHole(Hole hole) {
    return new HoleDTO(
        hole.getHoleNumber(),
        hole.getHoleNumber(),
        hole.getDistance(),
        hole.getPar());
  }

  private static CourseSummaryDTO toSummary(Course course) {
    return new CourseSummaryDTO(
        course.getExternalId(),
        course.getName(),
        calculateCoursePar(course),
        course.getHoles().size());
  }

  private static int calculateCoursePar(Course course) {
    return course.getHoles().stream().mapToInt(Hole::getPar).sum();
  }

}
