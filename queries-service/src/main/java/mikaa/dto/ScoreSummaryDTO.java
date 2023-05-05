package mikaa.dto;

public record ScoreSummaryDTO(
    long playerId,
    int result,
    int roundScore) {
}
