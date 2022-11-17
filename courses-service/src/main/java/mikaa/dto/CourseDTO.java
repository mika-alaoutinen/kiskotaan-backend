package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseDTO(long id, String name, HoleDTO hole) {
}
