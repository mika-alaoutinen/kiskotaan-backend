package mikaa.feature;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseNameDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewCourseDTO;
import mikaa.dto.NewCourseNameDTO;
import mikaa.dto.NewHoleDTO;

@ApplicationScoped
@Blocking
@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class CourseResource {

  private final CourseService service;
  private final HoleService holeService;

  @GET
  public RestResponse<List<CourseSummaryDTO>> getCourses() {
    return RestResponse.ok(service.findAll());
  }

  @GET
  @Path("/{id}")
  public RestResponse<CourseDTO> getCourse(@PathParam("id") long id) {
    return RestResponse.ok(service.findOne(id));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public RestResponse<CourseDTO> addCourse(@Valid NewCourseDTO newCourse) {
    var savedCourse = service.add(newCourse);
    return RestResponse.status(Status.CREATED, savedCourse);
  }

  @POST
  @Path("/{id}/holes")
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public RestResponse<HoleDTO> addHole(@PathParam("id") Long id, @Valid NewHoleDTO newHole) {
    return RestResponse.status(Status.CREATED, holeService.add(id, newHole));
  }

  @PATCH
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public RestResponse<CourseNameDTO> updateCourseName(@PathParam("id") long id, @Valid NewCourseNameDTO newName) {
    return RestResponse.ok(service.updateCourseName(id, newName.name()));
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  public RestResponse<Void> delete(@PathParam("id") long id) {
    service.delete(id);
    return RestResponse.noContent();
  }

}
