package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.dto.NewCourseDTO;
import mikaa.errors.NotFoundException;

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
    var course = service.findOne(id).orElseThrow(() -> notFound(id));
    return Response.ok(course).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public Response addCourse(@Valid NewCourseDTO newCourse) {
    var savedCourse = service.add(newCourse);
    return Response.status(Status.CREATED).entity(savedCourse).build();
  }

  @PATCH
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public Response updateCourseName(@PathParam("id") long id, @NotBlank String name) {
    String updatedName = service.updateCourseName(id, name).orElseThrow(() -> notFound(id));
    return Response.ok(updatedName).build();
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find course with id " + id;
    String path = "/courses/" + id;
    return new NotFoundException(msg, path);
  }

}
