package mikaa.events.hole;

public record HolePayload(long id, long courseId, int number, int par, int distance) {
}
