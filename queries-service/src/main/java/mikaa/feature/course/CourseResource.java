package mikaa.feature.course;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Path("courses")
@RequiredArgsConstructor
public class CourseResource {

  @GET
  @Path("/{id}")
  public Uni<String> getCourse(int id) {
    return Uni.createFrom().item("get one");
  }

  @GET
  public Multi<String> getCourses(
      @RestQuery String name,
      @RestQuery Integer holesMin,
      @RestQuery Integer holesMax,
      @RestQuery Integer parMin,
      @RestQuery Integer parMax) {
    return Multi.createFrom().item("get many");
  }

}
