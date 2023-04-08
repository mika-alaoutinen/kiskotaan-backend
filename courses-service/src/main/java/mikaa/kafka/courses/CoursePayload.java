package mikaa.kafka.courses;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.kafka.holes.HoleDTO;

@RegisterForReflection
public record CoursePayload(Long id, String name, List<HoleDTO> holes) {
}
