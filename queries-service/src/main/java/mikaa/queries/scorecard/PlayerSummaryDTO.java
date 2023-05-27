package mikaa.queries.scorecard;

record PlayerSummaryDTO(
    long id,
    String firstName,
    String lastName,
    int result,
    int roundScore) {
}
