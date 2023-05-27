package mikaa.queries.dto;

public record ScoreSummaryDTO(
    long playerId,
    int result,
    int roundScore) {
}
