package mikaa.feature.courses;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.domain.Course;

@ApplicationScoped
@GraphQLApi
@RequiredArgsConstructor
public class CourseResource {

  private final CourseService service;

  @Description("Find all courses")
  @Query
  public Collection<Course> courses() {
    return service.streamCourses();
  }

  @Description("Find a single course by ID")
  @Query
  public Optional<Course> course(long id) {
    return service.findCourse(id);
  }

}
