package mikaa.events.course;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CoursePayload(Long id, String name, List<Hole> holes) {
}