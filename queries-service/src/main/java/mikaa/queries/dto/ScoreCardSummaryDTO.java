package mikaa.queries.dto;

import java.util.List;

public record ScoreCardSummaryDTO(
        long id,
        CourseSummaryDTO course,
        List<PlayerDTO> players,
        List<ScoreSummaryDTO> scores) {
}
