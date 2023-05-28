package mikaa.queries.scorecard;

import java.util.List;

import mikaa.queries.dto.CourseDTO;

record ScoreCardDTO(
    long id,
    CourseDTO course,
    List<PlayerDTO> players) {
}
