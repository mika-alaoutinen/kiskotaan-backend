package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository repository;

  public CourseEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

}
