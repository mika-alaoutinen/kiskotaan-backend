package mikaa.events.hole;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record HolePayload(long id, long courseId, int number, int par, int distance) {
}
