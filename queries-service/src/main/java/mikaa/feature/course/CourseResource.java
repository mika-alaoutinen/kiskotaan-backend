package mikaa.feature.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class CourseResource {

  public Uni<String> hello() {
    return Uni.createFrom().item("Reactive courses");
  }

  public Uni<Void> getCourse(int id) {
    throw new UnsupportedOperationException("Unimplemented method 'getCourse'");
  }

  public Multi<Void> getCourses(
      String name,
      Integer holesMin,
      Integer holesMax,
      Integer parMin,
      Integer parMax) {
    throw new UnsupportedOperationException("Unimplemented method 'getCourses'");
  }

}
