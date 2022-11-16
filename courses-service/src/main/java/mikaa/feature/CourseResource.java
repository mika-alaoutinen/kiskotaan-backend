package mikaa.feature;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/courses")
public class CourseResource {

  private final CourseService service;

  public CourseResource(CourseService service) {
    this.service = service;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CourseSummary> findCourses() {
    return service.findAll();
  }

}
