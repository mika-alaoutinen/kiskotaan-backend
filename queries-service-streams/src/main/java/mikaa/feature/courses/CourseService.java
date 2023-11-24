package mikaa.feature.courses;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.streams.InteractiveQueries;
import mikaa.streams.KafkaStreamsConfig;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {

  private final InteractiveQueries queries;
  private final KafkaStreamsConfig config;

  Collection<Course> streamCourses() {
    Stream<CoursePayload> courses = queries.streamAll(storeName());
    return courses.map(Course::fromPayload).toList();
  }

  Optional<Course> findCourse(long id) {
    Optional<CoursePayload> maybeCourse = queries.findById(id, storeName());
    return maybeCourse.map(Course::fromPayload);
  }

  private String storeName() {
    return config.stateStores().courses();
  }

}
