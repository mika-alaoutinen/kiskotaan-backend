package mikaa.queries.course;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;

@ApplicationScoped
@Path("courses")
@RequiredArgsConstructor
public class CourseResource {

  private final CourseReader courses;

  @GET
  @Path("/{id}")
  public Uni<CourseDTO> getCourse(int id) {
    return courses.findOne(id).map(CourseMapper::toCourse);
  }

  @GET
  public Multi<CourseSummaryDTO> getCourses(
      @RestQuery String name,
      @RestQuery Integer holesMin,
      @RestQuery Integer holesMax,
      @RestQuery Integer parMin,
      @RestQuery Integer parMax) {
    return courses.findAll().map(CourseMapper::toSummary);
  }

}
