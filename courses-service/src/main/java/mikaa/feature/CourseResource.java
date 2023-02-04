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
