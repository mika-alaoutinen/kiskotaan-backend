package mikaa.queries.dto;

import java.util.List;

public record ScoreCardDTO(
    long id,
    CourseDTO course,
    List<ScoreCardPlayerDTO> players) {
}
