package mikaa.kafka.holes;

public record HolePayload(Long id, Long courseId, int holeNumber, int par, int distance) {
}
