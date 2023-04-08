package mikaa.kafka.holes;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record HoleDTO(Long id, int number, int par, int distance) {
}
