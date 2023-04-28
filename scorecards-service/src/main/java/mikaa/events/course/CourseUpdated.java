package mikaa.events.course;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseUpdated(Long id, String name) {
}
