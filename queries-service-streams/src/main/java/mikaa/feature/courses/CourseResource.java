package mikaa.feature.courses;

import java.util.Collection;
import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@GraphQLApi
public class CourseResource {

  private static final Course COURSE = new Course(1, "Laajis", 50);

  @Description("Find all courses")
  @Query
  public Uni<Collection<Course>> courses() {
    return Uni.createFrom().item(List.of(COURSE));
  }

  @Description("Find a single course by ID")
  @Query
  public Uni<Course> course(long id) {
    return Uni.createFrom().item(COURSE);
  }

}
