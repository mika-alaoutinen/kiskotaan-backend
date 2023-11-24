package mikaa.feature.courses;

import java.util.Collection;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.streams.InteractiveQueries;

@ApplicationScoped
@GraphQLApi
@RequiredArgsConstructor
public class CourseResource {

  private static final Course COURSE = new Course(1, "Laajis", 50);
  private final InteractiveQueries queries;

  @Description("Find all courses")
  @Query
  public Collection<Course> courses() {
    return queries.allCourses().map(Course::fromPayload).toList();
  }

  @Description("Find a single course by ID")
  @Query
  public Course course(long id) {
    return COURSE;
  }

}
