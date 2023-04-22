package mikaa.events.holes;

public record HolePayload(Long id, Long courseId, int number, int par, int distance) {
}
