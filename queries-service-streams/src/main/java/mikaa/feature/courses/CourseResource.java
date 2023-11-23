package mikaa.feature.courses;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@GraphQLApi
public class CourseResource {

  private static final Course COURSE = new Course(1, "Course name", 50);

  @Description("Find all courses")
  @Query
  public Multi<Course> courses() {
    return Multi.createFrom().item(COURSE);
  }

  @Description("Find a single course by ID")
  @Query
  public Uni<Course> course(long id) {
    return Uni.createFrom().item(COURSE);
  }

}
