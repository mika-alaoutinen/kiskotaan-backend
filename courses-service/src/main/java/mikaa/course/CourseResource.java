package mikaa.course;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Blocking
@Path("/courses")
@RequiredArgsConstructor
public class CourseResource {

  private final CourseService service;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CourseSummary> findCourses() {
    return service.findAll();
  }

}
