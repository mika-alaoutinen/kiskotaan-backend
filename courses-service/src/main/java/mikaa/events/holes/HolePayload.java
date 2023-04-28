package mikaa.events.holes;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record HolePayload(Long id, Long courseId, int number, int par, int distance) {
}
