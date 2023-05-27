package mikaa.queries.scorecard;

import java.util.List;

import mikaa.queries.dto.CourseSummaryDTO;

public record ScoreCardSummaryDTO(
    long id,
    CourseSummaryDTO course,
    List<PlayerSummaryDTO> players) {
}
