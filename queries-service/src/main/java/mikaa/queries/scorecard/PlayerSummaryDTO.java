package mikaa.queries.scorecard;

public record PlayerSummaryDTO(
    long id,
    String firstName,
    String lastName,
    int result,
    int roundScore) {
}
