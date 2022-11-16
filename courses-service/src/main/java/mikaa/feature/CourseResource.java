package mikaa.feature;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/courses")
public class CourseResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CourseEntity> findCourses() {
    return CourseEntity.listAll();
  }

}
