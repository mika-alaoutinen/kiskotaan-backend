package mikaa.feature.course;

import java.util.List;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.feature.MockData;

@ApplicationScoped
@Path("courses")
@RequiredArgsConstructor
public class CourseResource {

  @GET
  @Path("/{id}")
  public Uni<CourseDTO> getCourse(int id) {
    return Uni.createFrom().item(MockData.COURSE);
  }

  @GET
  public Uni<List<CourseDTO>> getCourses(
      @RestQuery String name,
      @RestQuery Integer holesMin,
      @RestQuery Integer holesMax,
      @RestQuery Integer parMin,
      @RestQuery Integer parMax) {
    return Uni.createFrom().item(List.of(MockData.COURSE));
  }

}
