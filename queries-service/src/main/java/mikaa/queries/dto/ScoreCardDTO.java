package mikaa.queries.dto;

import java.util.List;
import java.util.Map;

public record ScoreCardDTO(
                long id,
                CourseDTO course,
                List<PlayerDTO> players,
                Map<Long, List<ScoreDTO>> scores) {
}
