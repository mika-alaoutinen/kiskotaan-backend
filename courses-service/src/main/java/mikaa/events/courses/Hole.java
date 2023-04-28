package mikaa.events.courses;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Hole(Long id, int number, int par, int distance) {
}
