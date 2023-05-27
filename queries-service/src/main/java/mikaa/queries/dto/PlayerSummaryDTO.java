package mikaa.queries.dto;

public record PlayerSummaryDTO(
    long id,
    String firstName,
    String lastName,
    int result,
    int roundScore) {
}
