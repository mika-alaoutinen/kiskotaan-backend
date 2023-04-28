package mikaa.events.course;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseAdded(Long id, String name, List<Hole> holes) {
}
