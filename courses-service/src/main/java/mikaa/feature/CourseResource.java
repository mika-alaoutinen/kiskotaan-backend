package mikaa.feature;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseNameDTO;
import mikaa.dto.CourseSummaryDTO;
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
  public RestResponse<List<CourseSummaryDTO>> getCourses() {
    return RestResponse.ok(service.findAll());
  }

  @GET
  @Path("/{id}")
  public RestResponse<CourseDTO> getCourse(@PathParam("id") long id) {
    var course = service.findOne(id).orElseThrow(() -> notFound(id));
    return RestResponse.ok(course);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public RestResponse<CourseDTO> addCourse(@Valid NewCourseDTO newCourse) {
    var savedCourse = service.add(newCourse);
    return RestResponse.status(Status.CREATED, savedCourse);
  }

  @PATCH
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public RestResponse<CourseNameDTO> updateCourseName(@PathParam("id") long id, @Valid CourseNameDTO courseName) {
    var updatedName = service.updateCourseName(id, courseName.name())
        .orElseThrow(() -> notFound(id));
    return RestResponse.ok(updatedName);
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  public RestResponse<Void> delete(@PathParam("id") long id) {
    service.delete(id);
    return RestResponse.noContent();
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find course with id " + id;
    String path = "/courses/" + id;
    return new NotFoundException(msg, path);
  }

}
