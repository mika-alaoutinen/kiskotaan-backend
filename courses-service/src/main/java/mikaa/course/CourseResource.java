package mikaa.course;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Blocking
@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class CourseResource {

  private final CourseService service;

  @GET
  public Response getCourses() {
    return Response.ok(service.findAll()).build();
  }

  @GET
  @Path("/{id}")
  public Response getCourse(@PathParam("id") long id) {
    return Response.noContent().build();
  }
}
