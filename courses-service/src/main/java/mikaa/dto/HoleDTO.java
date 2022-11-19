package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record HoleDTO(long id, int number, int par, int distance) {
}
