package mikaa.feature.course;

import java.math.BigDecimal;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.errors.NotFoundException;

@ApplicationScoped
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository repository;

  public CourseEntity findOrThrow(BigDecimal id) {
    long courseId = id.longValue();
    return repository.findByIdOptional(courseId).orElseThrow(() -> notFound(courseId));
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

}