package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record HoleDTO(Long id, Long courseId, int number, int par, int distance) {
}
