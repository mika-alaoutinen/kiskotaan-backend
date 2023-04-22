package mikaa.events.courses;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseWithoutHoles(Long id, String name) {
}
